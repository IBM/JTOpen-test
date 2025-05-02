///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  IFSThreadTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test.Thread;


import java.io.FileOutputStream;
import java.io.PipedInputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import com.ibm.as400.access.*;

import java.util.Vector;
import com.ibm.as400.access.AS400;

import test.misc.ThreadedTestcase;


public class IFSThreadTestcase
  extends ThreadedTestcase
{
  private static String lib         = "IFSTT.LIB";  // library on as400 to conduct tests in.
  private static String ifsDirName  = "/";
  private static String fileName    = "File";
  private static String ifsPathName = ifsDirName + fileName;

 /**
  * Creates a new IFSThreadTestcase.
  * This is called from ThreadTest::createTestcases().
  */
  public IFSThreadTestcase (AS400 systemObject,
                             Vector<String> variationsToRun,
                             int runMode,
                             FileOutputStream fileOutputStream,
                             
                             String password)
  {
    super (systemObject, "IFSThreadTestcase", 9, variationsToRun,
           runMode, fileOutputStream, password);
  }


  void createFile(String pathName)
  {
    try
    {
      IFSFile file = new IFSFile(systemObject_, pathName);
      if (file.exists())
      {
        file.delete();
      }
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, pathName, "rw");
      raf.close();
    }
    catch(Exception e)
    {}
  }


  void deleteFile(String pathName)
  {
    try
    {
      IFSFile file = new IFSFile(systemObject_, pathName);
      if (file.exists())
      {
        file.delete();
      }
    }
    catch(Exception e)
    {}
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
      System.out.println("Setup failed");
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

    try
    {
      cleanup();
    }
    catch (Exception e){}
  }


  // create library, and file it with basic files and *.txt files.
  protected void setup()
    throws Exception
  {
      lockSystem("IFSFILE", 600); 
    try
    {
      CommandCall cmd = new CommandCall(systemObject_);
      deleteLibrary(cmd, lib); 
      if (cmd.run("QSYS/CRTLIB " + lib) == false)
        output_.println( "Unable to create library "
                         + cmd.getMessageList()[0].getID()
                         + cmd.getMessageList()[0].getText());
      systemObject_.disconnectService(AS400.COMMAND);

      // create file1 thru file10
      for (int i=1; i<=10; i++)
        createFile("/QSYS.LIB/" + lib + "/" + fileName + i);

      // create file1.txt thru file10.txt
      for (int i=1; i<=10; i++)
        createFile("/QSYS.LIB/" + lib + "/" + fileName + i + ".txt");
    }
    catch (Exception e)
    {
      output_.println("Unable to do setup:" + e);
      throw e;
    }
  }


  // delete basic files and *.txt files from library create in setup().
  protected void cleanup()
    throws Exception
  {
    try
    {
      // delete file1 thru file10
      for (int i=1; i<=10; i++)
        deleteFile("/QSYS.LIB/" + lib + "/" + fileName + i);

      // create file1.txt thru file10.txt
      for (int i=1; i<=10; i++)
        deleteFile("/QSYS.LIB/" + lib + "/" + fileName + i + ".txt");
    }
    catch (Exception e)
    {
      output_.println("Cleanup failed:" + e);
      throw e;
    }
    unlockSystem();

  }



 /**
  * Multiple Threads perform list() on the same dir using same IFSFile object.
  */
  public void Var001()
  {
    try
    {
      pipeInput_    = new PipedInputStream();
      IFSFile    f  = new IFSFile(systemObject_, "/QSYS.LIB/" + lib);
      // create a directory listing to compare against
      String[]   l  = f.list();
      IFSThread  t1 = new IFSThread(pipeInput_, output_, this, IFSThread.LIST, f, l);
      IFSThread  t2 = new IFSThread(pipeInput_, output_, this, IFSThread.LIST, f, l);
      IFSThread  t3 = new IFSThread(pipeInput_, output_, this, IFSThread.LIST, f, l);
      objectInput_  = new ObjectInputStream(pipeInput_);
      t1.start();
      t2.start();
      t3.start();
      go();
      handleError();
      stopThreads();
    }
    catch(IOException e)
    {
      failed(e, "Unexpected Exception");
    }
  }


 /**
  * Multiple Threads perform list(String) on the same dir
  * using same IFSFile object but different filters.  Verify
  * threads don't recieve wrong listing.
  */
  public void Var002()
  {
    try
    {
      pipeInput_      = new PipedInputStream();
      IFSFile    file = new IFSFile(systemObject_, "/QSYS.LIB/" + lib);
      // create a directory listing to compare against
      String[]   listAll = file.list();
      String     filter  = "*.txt"; // only list .pgm's
      String[]   filteredList = file.list(filter);
      IFSThread  t1 = new IFSThread(pipeInput_, output_, this,
                                    IFSThread.LIST, file, listAll);
      IFSThread  t2 = new IFSThread(pipeInput_, output_, this,
                                    IFSThread.FILTERED_LIST,
                                    file, filteredList, filter);
      objectInput_  = new ObjectInputStream(pipeInput_);
      t1.start();
      t2.start();
      go();
      handleError();
      stopThreads();
    }
    catch(IOException e)
    {
      failed(e, "Unexpected Exception");
    }
  }


 /**
  * verify that lock on a data segment in a file holds between threads.
  * Threads don't share the IFS stream.
  */
  public void Var003()
  {
    try
    {
      pipeInput_      = new PipedInputStream();
      createFile(ifsPathName);
      IFSThread  t1 = new IFSThread(pipeInput_, output_, this,
                                    IFSThread.LOCKED_RD_WR, ifsPathName, systemObject_);
      IFSThread  t2 = new IFSThread(pipeInput_, output_, this,
                                    IFSThread.LOCKED_RD_WR, ifsPathName, systemObject_);
      objectInput_  = new ObjectInputStream(pipeInput_);

      t1.start();
      t2.start();
      go();
      handleError();
      stopThreads();
      deleteFile(ifsPathName);
    }
    catch(IOException e)
    {
      failed(e, "Unexpected Exception");
    }
  }


 /**
  * Seperate Threads performing lists, filtered lists, and locked read writes.
  */
  public void Var004()
  {
    try
    {
      pipeInput_      = new PipedInputStream();
      IFSFile    file = new IFSFile(systemObject_, "/QSYS.LIB/" + lib);
      // create a directory listing to compare against
      String[]   listAll = file.list();
      String     filter  = "*.txt";
      String[]   filteredList = file.list(filter);
      IFSThread  t1 = new IFSThread(pipeInput_, output_, this,
                                    IFSThread.LIST, file, listAll);
      IFSThread  t2 = new IFSThread(pipeInput_, output_, this,
                                    IFSThread.FILTERED_LIST,
                                    file, filteredList, filter);
      createFile(ifsPathName);
      IFSThread  t3 = new IFSThread(pipeInput_, output_, this,
                                    IFSThread.LOCKED_RD_WR, ifsPathName, systemObject_);
      objectInput_  = new ObjectInputStream(pipeInput_);
      t1.start();
      t2.start();
      t3.start();
      go();
      handleError();
      stopThreads();
      deleteFile(ifsPathName);
    }
    catch(IOException e)
    {
      failed(e, "Unexpected Exception");
    }
  }


 /**
  * Inspect Directory
  */
  public void Var005()
  {
    try
    {
      pipeInput_      = new PipedInputStream();
      IFSFile    dir  = new IFSFile(systemObject_, "/QSYS.LIB/" + lib);
      long       mod  = dir.lastModified();
      IFSThread  t1   = new IFSThread(pipeInput_, output_, this,
                                    IFSThread.INSPECT, dir, mod);
      IFSThread  t2   = new IFSThread(pipeInput_, output_, this,
                                    IFSThread.INSPECT, dir, mod);
      objectInput_  = new ObjectInputStream(pipeInput_);
      t1.start();
      t2.start();
      go();
      handleError();
      stopThreads();
    }
    catch( Exception e )
    {
      failed(e, "Unexpected Exception");
    }
  }


 /**
  * Inspect File
  */
  public void Var006()
  {
    try
    {
      pipeInput_      = new PipedInputStream();
      createFile(ifsPathName);
      IFSFile    file = new IFSFile(systemObject_, ifsPathName);
      long       mod  = file.lastModified();
      IFSThread  t1   = new IFSThread(pipeInput_, output_, this,
                                    IFSThread.INSPECT, file, mod);
      IFSThread  t2   = new IFSThread(pipeInput_, output_, this,
                                    IFSThread.INSPECT, file, mod);
      objectInput_  = new ObjectInputStream(pipeInput_);
      t1.start();
      t2.start();
      go();
      handleError();
      stopThreads();
      deleteFile(ifsPathName);
    }
    catch( Exception e )
    {
      failed(e, "Unexpected Exception");
    }
  }


 /**
  * perform inspections on directory and at the same time perform lists
  * on the directory.
  */
  public void Var007()
  {
    try
    {
      pipeInput_      = new PipedInputStream();
      IFSFile    file = new IFSFile(systemObject_, "/QSYS.LIB/" + lib);
      // create a directory listing to compare against
      String[]   listAll = file.list();
      String     filter  = "*.txt"; // only list .pgm's
      String[]   filteredList = file.list(filter);
      long       mod  = file.lastModified();
      IFSThread  t1 = new IFSThread(pipeInput_, output_, this,
                                    IFSThread.LIST, file, listAll);
      IFSThread  t2 = new IFSThread(pipeInput_, output_, this,
                                    IFSThread.FILTERED_LIST,
                                    file, filteredList, filter);
      IFSThread  t3 = new IFSThread(pipeInput_, output_, this,
                                    IFSThread.INSPECT, file, mod);
      objectInput_  = new ObjectInputStream(pipeInput_);
      t1.start();
      t2.start();
      t3.start();
      go();
      handleError();
      stopThreads();
    }
    catch( Exception e )
    {
      failed(e, "Unexpected Exception");
    }
  }


 /**
  * 3 Threads performing directory manipulations (create, touch,
  * rename, delete).  Each Thread uses it's own IFSFile objects.
  */
  public void Var008()
  {
    try
    {
      pipeInput_    = new PipedInputStream();
      // generate unique dir names
      String     d1 = "/IFS8D1.LIB";
      IFSThread  t1 = new IFSThread(pipeInput_, output_, this,
                                    IFSThread.DIR_MANIP, d1, systemObject_);
      objectInput_  = new ObjectInputStream(pipeInput_);
      t1.start();
      go();
      handleError();
      stopThreads();
    }
    catch( Exception e )
    {
      failed(e, "Unexpected Exception");
    }
  }


 /**
  *
  */
  public void Var009()
  {
    try
    {
      pipeInput_    = new PipedInputStream();
      IFSFile    f1 = new IFSFile(systemObject_, "/QSYS.LIB/" + lib);
      IFSFile    f2 = new IFSFile(systemObject_, "/JDBCTEST.LIB");
      createFile(ifsPathName);
      IFSFile    f3 = new IFSFile(systemObject_, ifsPathName);
      IFSThread  t1 = new IFSThread(pipeInput_, output_, this,
                                    IFSThread.LS_LONG, f1);
      IFSThread  t2 = new IFSThread(pipeInput_, output_, this,
                                    IFSThread.LS_LONG, f2);
      IFSThread  t3 = new IFSThread(pipeInput_, output_, this,
                                    IFSThread.LS_LONG, f3);
      objectInput_  = new ObjectInputStream(pipeInput_);
      t1.start();
      t2.start();
      t3.start();
      go();
      handleError();
      stopThreads();
      deleteFile(ifsPathName);
    }
    catch( Exception e )
    {
      failed(e, "Unexpected Exception");
    }
  }
}




