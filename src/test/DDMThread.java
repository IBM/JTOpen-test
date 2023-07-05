///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DDMThread.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.io.PipedInputStream;
import java.io.PrintWriter;
import com.ibm.as400.access.*;
import java.util.*;
import java.io.*;


class DDMThread
  extends ComponentThread
{
  // Functions that this thread can perform.  One is specified at object construction.
  public static final int OPEN_KEYED           = 1;
  public static final int OPEN_SEQ             = 2;
  public static final int CONSUMER_SEQ         = 3;
  public static final int PRODUCER_SEQ         = 4;
  public static final int CONSUMER_KEYED       = 5;
  public static final int PRODUCER_KEYED       = 6;
  public static final int OPEN_KEYED_SHARED    = 7;
  public static final int OPEN_SEQ_SHARED      = 8;
  public static final int READ_ALL_KEYED       = 9;
  public static final int READ_ALL_SEQ         = 10;

  private   AS400File      file              = null;
  private   SequentialFile seqFile           = null;
  private   KeyedFile      keyFile           = null;
  private   AS400          system            = null;
  private   String         path              = null;
  private   Record[]       origRecords       = null;  // array of recs for comparison in READ_ALL_X
  private   int            deletedCount      = 0;     // num deleted recs in consumerSequential
  private   int            openType;
  private   int            blockingFactor;
  private   int            commit;


 /**
  * Constructor
  */
  public DDMThread(PipedInputStream pipeReader, PrintWriter output,
                   ThreadedTestcase testcase,  int function)
  {
    super(pipeReader, output, testcase, function);
  }


 /**
  * Constructor for OPEN_KEYED, OPEN_SEQ
  * file specified by path should have already been created.
  */
  public DDMThread(PipedInputStream pipeReader, PrintWriter output,
                   ThreadedTestcase testcase,  int function, AS400 sys,
                   String path, int openType, int blockingFactor,
                   int commit)
  {
    super(pipeReader, output, testcase, function);
    this.system         = sys;
    this.path           = path;
    this.openType       = openType;
    this.blockingFactor = blockingFactor;
    this.commit         = commit;
  }


 /**
  * Constructor for OPEN_KEYED_SHARED, OPEN_SEQ_SHARED
  */
  public DDMThread(PipedInputStream pipeReader, PrintWriter output,
                   ThreadedTestcase testcase,  int function, int openType,
                   int blockingFactor, int commit, AS400File file)
  {
    super(pipeReader, output, testcase, function);
    this.openType       = openType;
    this.blockingFactor = blockingFactor;
    this.commit         = commit;
    this.file           = file;
  }


 /**
  * Constructor for CONSUMER_SEQ, PRODUCER_SEQ.
  */
  public DDMThread(PipedInputStream pipeReader, PrintWriter output,
                   ThreadedTestcase testcase,  int function, SequentialFile file)
  {
    super(pipeReader, output, testcase, function);
    seqFile = file;
  }


 /**
  * Constructor for CONSUMER_KEYED, PRODUCER_KEYED.
  */
  public DDMThread(PipedInputStream pipeReader, PrintWriter output,
                   ThreadedTestcase testcase,  int function, KeyedFile file)
  {
    super(pipeReader, output, testcase, function);
    keyFile = file;
  }


 /**
  * Constructor for READ_ALL_KEYED
  */
  public DDMThread(PipedInputStream pipeReader, PrintWriter output,
                   ThreadedTestcase testcase,  int function,
                   KeyedFile file, Record[] origRecs)
  {
    super(pipeReader, output, testcase, function);
    keyFile     = file;
    origRecords = origRecs;
  }


 /**
  * Constructor for READ_ALL_SEQ
  */
  public DDMThread(PipedInputStream pipeReader, PrintWriter output,
                   ThreadedTestcase testcase,  int function,
                   SequentialFile file, Record[] origRecs)
  {
    super(pipeReader, output, testcase, function);
    seqFile     = file;
    origRecords = origRecs;
  }


  public void run()
  {
    // notify testcase that we are ready to start.
    testcase_.ready();

    // perform the task specified by function, numLoops
    // times unless the stop flag is set.
    for (int i = 0; i < numLoops_ && !stop_ ; i++)
    {
      // don't be a selfish thread...
      try { sleep(1); } catch (InterruptedException e) {}
output_.println(getName() + " " + i);
      switch (function_)
      {
        case OPEN_SEQ:
          performOpen();
          break;
        case OPEN_KEYED:
          performOpen();
          break;
        case OPEN_SEQ_SHARED:
          performOpenShared();
          break;
        case OPEN_KEYED_SHARED:
          performOpenShared();
          break;
        case CONSUMER_SEQ:
          performConsumerSequential();
          break;
        case PRODUCER_SEQ:
          performProducerSequential();
          break;
        case CONSUMER_KEYED:
          performConsumerKeyed();
          break;
        case PRODUCER_KEYED:
          performProducerKeyed();
          break;
        case READ_ALL_KEYED:
          performReadAll();
          break;
        case READ_ALL_SEQ:
          performReadAll();
          break;
      }
    }

    // done: close our end of pipe and stop.
    this.kill();
  }


 /**
  * Attempts to open a file using a locally created SequentialFile
  * or KeyedFile (depending on function specified in constructor).
  * Then values set on open are verified.
  */
  private void performOpen()
  {
    try
    {
      if (function_ == OPEN_SEQ)
      {
        file = new SequentialFile(system, path);
        file.setRecordFormat( new DDMChar10NoKeyFormat(system));
      }
      else // function_ == OPEN_KEYED
      {
        file = new KeyedFile(system,path);
        file.setRecordFormat( new DDMChar10KeyFormat(system));
      }

      if (commit != AS400File.COMMIT_LOCK_LEVEL_NONE)
      {
        output_.println(getName() + " b4 starting commitment control");
        file.startCommitmentControl(commit);
      }

      file.open(openType, blockingFactor, commit);
      output_.println(getName()+"openType: "+openType+"  read: "+file.isReadOnly()+" write: "+file.isWriteOnly()+" both: "+file.isReadWrite());
      output_.println(getName()+"blockingFactor: "+blockingFactor+"  new: "+file.getBlockingFactor());
      output_.println(getName()+"commit: "+commit+"  new: "+file.getCommitLockLevel());
      output_.println(getName() + " after open");

      output_.println(getName() + " opened: b4 isOpen");
      if (!file.isOpen())
      {
        error("File not Open after successful open");
        return;
      }
      // When READ_WRITE is specified during open or file is a KeyedFile then there is no blocking.
      output_.println(getName() + " opened: b4 checking blockingfactor");
      if (openType == AS400File.READ_WRITE || file instanceof KeyedFile)
      {
        if (file.getBlockingFactor() != 1)
      {
        error("Blocking factor != 1 : " + file.getBlockingFactor());
        return;
      }
      }
      else
      {
        // For READ_ONLY & WRITE_ONLY, blocking factor should be what
        // was specified during call to open.
          if (file.getBlockingFactor() != blockingFactor)
          {
            error("Blocking factor != " + blockingFactor + " : "
                              + file.getBlockingFactor());
            return;
          }

      }
      if (file.getCommitLockLevel() != commit &&
          commit != AS400File.COMMIT_LOCK_LEVEL_NONE)
      {
        error("Commit Lock Level != " + commit + " : "
                        + file.getCommitLockLevel());
        return;
      }

      output_.println(getName() + " opened: b4 checking openType");
      switch(openType)
      {
        case AS400File.READ_ONLY:
          if (!file.isReadOnly())
          {
            error("file not read-only as expected");
            return;
          }
          break;
        case AS400File.READ_WRITE:
          if (!file.isReadWrite())
          {
            error("file not read-write as expected");
            return;
          }
         break;
        case AS400File.WRITE_ONLY:
          if (!file.isWriteOnly())
          {
            error("file not write-only as expected");
            return;
          }
          break;
      } // end switch(openType)
    }
    catch (Exception e)
    {
      error("Unexpected Exception " + getName() + e, e);
    }
    finally
    {
      try
      {
        if (file != null && file.isOpen())
        {
          if (commit != AS400File.COMMIT_LOCK_LEVEL_NONE)
          {
            output_.println(getName()+" commiting & closing");
            file.commit();
            file.close();
            file.endCommitmentControl();
          }
          else
          {
            output_.println(getName()+" closing");
            file.close();
          }
        }
      }
      catch (Exception e)
      {
        output_.println(getName()+" Unable to close file "+e);
      }
      output_.println(getName()+" out of finally");
    }
  }


 /**
  * Writes a record to the file.  No more than one record will exist
  * in the file as the producer waits until the consumer 'eats' our record
  * before another is written to the file. Assumes that commitment
  * level is set to CHANGE.  Note that exactly numLoops_
  * records will be written to the file.
  */
  private void performProducerSequential()
  {
    try
    {
      // make a record to write.
      Record recordToWrite = seqFile.getRecordFormat().getNewRecord();
      recordToWrite.setField(0, "PROD_SEQ");

      // wait until consumer 'eats' the record.
      Record readRecord = seqFile.readFirst();
      while (readRecord != null)
      {
        try { Thread.sleep(50); } catch (InterruptedException e) {}
        readRecord = seqFile.readFirst();
      }

      // consumer has 'eaten' the last record--we can now write.
      seqFile.write(recordToWrite);
      seqFile.commit();
    }
    catch (Exception e)
    {
      error("Unexpected Exception in " + getName() + e, e);
    }
  }


 /**
  * Reads and deletes the 1st record from the file.
  * If no record exists in the file then sleep.
  */
  private void performConsumerSequential()
  {
    try
    {
      Record record = seqFile.readFirst();
      while (record == null)
      {
        try { Thread.sleep(50); } catch (InterruptedException e) {}
        record = seqFile.readFirst();
      }

      // verify that record contains expected data.
      if ( ! ((String) record.getField(0)).startsWith("PROD_SEQ") )
      {
        error("Record contains incorrect data");
        return;
      }

      seqFile.deleteRecord(++deletedCount);
      seqFile.commit();
    }
    catch (Exception e)
    {
      error("Unexpected Exception in " + getName() + e, e);
    }
  }


 /**
  * Writes a keyed record to the file.  No more than one record will exist
  * in the file as the producer waits until the consumer 'eats' our record
  * before another is written to the file. Note that exactly numLoops_
  * records will be written to the file.
  */
  private void performProducerKeyed()
  {
    try
    {
      // make a record to write.
      Record recordToWrite = keyFile.getRecordFormat().getNewRecord();
      recordToWrite.setField(0, "PROD_KEY");
      Object[] key = {"PROD_KEY  "};
      // wait until consumer 'eats' the record.
      Record readRecord = keyFile.read(key);
output_.println(getName() + " b4 while readRecord="+readRecord);
      while (readRecord != null)
      {
        try { Thread.sleep(50); } catch (InterruptedException e) {}
        readRecord = keyFile.read(key);
output_.println(getName() + " in while readRecord="+readRecord);
      }

      // consumer has 'eaten' the last record--we can now write.
output_.println("writing");
      keyFile.write(recordToWrite);
      keyFile.commit();
    }
    catch (Exception e)
    {
      error("Unexpected Exception in " + getName() + e, e);
    }
  }


 /**
  * Reads and deletes the 1st record from the KeyedFile.
  * If no record matching the key exists then sleep and
  * try again.
  */
  private void performConsumerKeyed()
  {
    try
    {
      Object[] key = {"PROD_KEY  "};
      Record record = keyFile.read(key);
output_.println(getName() + " b4 while record=" + record);
      while (record == null)
      {
        try { Thread.sleep(50); } catch (InterruptedException e) {}
        record = keyFile.read(key);
output_.println(getName() + " in while record=" + record);
      }

output_.println("deleting");
      keyFile.deleteRecord(key);
      keyFile.commit();
output_.println("deleted");
    }
    catch (Exception e)
    {
      error("Unexpected Exception in " + getName() + e, e);
    }
  }



 /**
  * Attempts to open a file using a shared SequentialFile
  * or KeyedFile (depending on function specified in constructor).
  * If the thread is able to open the file it verifies that
  * all of the parameters are correct and then closes the file.
  * If it can't open the file (file is already open) then it
  * does nothing.
  */
  private void performOpenShared()
  {
    try
    {
      try
      {
output_.println(getName() + " b4 open");
        file.open(openType, blockingFactor, commit);
      }
      catch(ExtendedIllegalStateException e)
      {
output_.println("File is already open");
        // if another threads has already opened the file
        // a extendedIllegalStateException--this is ok.
        return;
      }

      // verify that state is correct
output_.println(getName() + " b4 isOpen");
      if (!file.isOpen())
      {
        error("File not Open after successful open");
        return;
      }

      // When READ_WRITE is specified during open there is no blocking.
output_.println(getName() + " opened: b4 checking blockingfactor");
      if (openType == AS400File.READ_WRITE)
      {
        if (file.getBlockingFactor() != 1)
        {
          error(getName()+" Blocking factor != 1 : " + file.getBlockingFactor());
          return;
        }
      }
      else
      {
        // For READ_ONLY & WRITE_ONLY, blocking factor should be what
        // was specified during call to open.
          if (file.getBlockingFactor() != blockingFactor)
          {
            error(getName()+" Blocking factor != " + blockingFactor + " : "
                              + file.getBlockingFactor());
            return;
          }

      }

      // verify the commit level
      if (file.getCommitLockLevel() != commit &&
          commit != AS400File.COMMIT_LOCK_LEVEL_NONE)
      {
        error("Commit Lock Level != " + commit + " : "
                        + file.getCommitLockLevel());
        return;
      }

output_.println(getName() + " opened: b4 checking openType");
      // verify openType
      switch(openType)
      {
        case AS400File.READ_ONLY:
          if (!file.isReadOnly())
          {
            error("file not read-only as expected");
            return;
          }
          break;
        case AS400File.READ_WRITE:
          if (!file.isReadWrite())
          {
            error("file not read-write as expected");
            return;
          }
         break;
        case AS400File.WRITE_ONLY:
          if (!file.isWriteOnly())
          {
            error("file not write-only as expected");
            return;
          }
          break;
      } // end switch(openType)

      file.close();
output_.println(getName()+" closing");
    }
    catch (Exception e)
    {
      error("Unexpected Exception " + getName() +" "+ e, e);
    }
  }


  private void performReadAll()
  {
    try
    {
      // read all the records in the file
      Record[] records = function_==READ_ALL_SEQ?seqFile.readAll():
                                                 keyFile.readAll();
      // verify the records
      if (origRecords.length != records.length)
      {
        error(getName() + " retrieved wrong # of records");
        return;
      }
      for(int i=0; i<records.length; i++)
        if ( ! records[i].toString().equals(origRecords[i].toString()) )
        {
          error(getName() + " records don't match" + records[i].toString() +"vs"
                                               + origRecords[i].toString());
          return;
        }
    }
    catch(Exception e)
    {
      error(getName() + " Unexpected Exception " +e,e);
    }
  }
}





