///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DDMProxyStressTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.DDM;

import java.io.PrintWriter;
import java.lang.Thread;
import com.ibm.as400.access.*;
import com.ibm.as400.access.Record;

import test.JDTestcase;
import test.PasswordVault;
import test.ProxyStressTest;


public class DDMProxyStressTestcase 
   extends ProxyStressTest implements Runnable 
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "DDMProxyStressTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.ProxyStressTest.main(newArgs); 
   }
                                 
// Private variables
   private Thread thread_;
   private boolean isRunning_;
   private int curntThread_;
   private CommandCall ddmlib_;

   AS400Message[] msglist_;
   Record[] records_;
   
   KeyedFile f2_;

   AS400 sys2_ = null; 

//Constructor
   public DDMProxyStressTestcase(int n, PrintWriter output) 
   {
      curntThread_ = n;
      thread_ = null;
      output_ = output; 
      setup();
      
         char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
   sys2_ = new AS400( systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
      

   }


/**
   Cleanup:  deletes the libraries created on the AS/400
 **/
   private void cleanup() 
   {
      CommandCall dlt = new CommandCall( sys2_);
      JDTestcase.deleteLibrary(dlt, "DDMPXY" + curntThread_);
      AS400Message[] msglist_ = null;
      try 
      {
         output_.println("     Deleting DDM libraries..." + "(t" + curntThread_ + ")");

         if (dlt.run() != true) 
         {
            msglist_ = dlt.getMessageList();
            
            output_.println("       Cleanup Failed."  + "(t" + curntThread_ + ")");
            
            if (Trace.isTraceOn())
               Trace.log(Trace.ERROR, msglist_[0].getID() + " " + msglist_[0].getText());
         }
      }
      catch(Exception e) 
      {
         output_.println("      Exception during cleanup." + "(t" + curntThread_ + ")");

         if (Trace.isTraceOn())
            Trace.log(Trace.ERROR, e);
      }
   }


/**
   Runs the current thread_
 **/  
   @SuppressWarnings("deprecation")
  public void run() 
   {
      //String readString = null;
      KeyedFile file = null;
      
      int i;

      //While thread_ is still running
      while (isRunning_) 
      {
         //loop until user specified maxIterations is reached
         for (i=1; i<=maxIterations_; i++) 
         {  
            try
            {
               output_.println("\n   Loop #: " + i + " (current thread: " + curntThread_ + ")");
               
               output_.println("     Creating KeyedFile..." + "(t" + curntThread_ + ")");

               file = new KeyedFile(sys2_, "/QSYS.LIB/DDMPXY" + 
                                    curntThread_ + ".LIB/KEYSRC.FILE/MBR1.MBR");
               file.setRecordFormat(new DDMKeyFormat(sys2_));

               output_.println("     Opening KeyedFile..." + "(t" + curntThread_ + ")");
               
               file.open(AS400File.READ_WRITE, 2, AS400File.COMMIT_LOCK_LEVEL_NONE);
               
               // Verify the object state
               if (!file.isOpen())
                  output_.println("isOpen() not returning true.\n");
               if (file.isReadOnly())
                  output_.println("isReadOnly() returning true.\n");
               if (!file.isReadWrite())
                  output_.println("isReadWrite() not returning true.\n");
               if (file.isWriteOnly())
                  output_.println("isWriteOnly() returning true.\n");
               if (file.getBlockingFactor() != 1)
                  output_.println("getBlockingFactor() failed\n");
               if (file.getCommitLockLevel() != -1)
                  output_.println("getCommitLockLevel() not returning -1: " + 
                                     String.valueOf(file.getCommitLockLevel()) + ".\n");
            }
            catch(Exception e)
            {
               output_.println("       Unexpected Exception." + "(t" + curntThread_ + ")");
               output_.println("   Loop #" + i + ": FAILED" + "(t" + curntThread_ + ")");
               if (Trace.isTraceOn())
                  Trace.log(Trace.ERROR, e);
            }
            
            try
            {
               output_.println("     Writing a record..." + "(t" + curntThread_ + ")");
               
               // Ensure that we can write a record.
               Record record = file.getRecordFormat().getNewRecord();
               file.write(record);
            }
            catch(Exception e)
            {
               output_.println("Exception writing to file\n");
               output_.println("   Loop #" + i + ": FAILED" + "(t" + curntThread_ + ")");

               if (Trace.isTraceOn())
                  Trace.log(Trace.ERROR, e);
               thread_.stop();
            }
            
            try
            {
               output_.println("     Reading a record..." + "(t" + curntThread_ + ")");

               // Ensure that we can read a record.
               @SuppressWarnings("unused")
              Record r = file.readFirst();
               r = file.readLast();
               r = file.readPrevious();
            }
            catch(Exception e)
            {
               output_.println("Exception reading from file\n");
               output_.println("   Loop #" + i + ": FAILED" + "(t" + curntThread_ + ")");

               if (Trace.isTraceOn())
                  Trace.log(Trace.ERROR, e);
               thread_.stop();
            }
            
            try
            {
               output_.println("     Updating a record..." + "(t" + curntThread_ + ")");
               
               // Ensure that we can update.
               Record record = file.getRecordFormat().getNewRecord();
               file.update(record);
            }
            catch(Exception e)
            {
               output_.println("Exception updating file\n");
               output_.println("   Loop #" + i + ": FAILED" + "(t" + curntThread_ + ")");

               if (Trace.isTraceOn())
                  Trace.log(Trace.ERROR, e);
               thread_.stop();
            }
            
            try
            {
               output_.println("     Deleting record..." + "(t" + curntThread_ + ")");

               // Ensure that we can delete the record.
               file.readLast(); // need this to reposition the cursor
               file.deleteCurrentRecord();
            }
            catch(Exception e)
            {
               output_.println("Exception deleting from file\n");
               output_.println("   Loop #" + i + ": FAILED" + "(t" + curntThread_ + ")");

               if (Trace.isTraceOn())
                  Trace.log(Trace.ERROR, e);
               thread_.stop();
            }
            
            // Close the file
            try
            {
               output_.println("     Closing KeyedFile..." + "(t" + curntThread_ + ")");

               file.close();

               output_.println("   Loop #" + i + ": Successful" + "(t" + curntThread_ + ")");
            }
            catch(Exception e)
            {
               output_.println("Failed to close file.\n");
               output_.println("   Loop #" + i + ": FAILED" + "(t" + curntThread_ + ")");

               if (Trace.isTraceOn())
                  Trace.log(Trace.ERROR, e);
               thread_.stop();
            }
            
            if (i == maxIterations_) 
            {
               cleanup();
               thread_.stop();
            }         
         }
      }

   }

/**
   Sets up the Library where the user space will reside
 **/

   private void setup() 
   {
      ddmlib_ = new CommandCall( sys2_, "QSYS/crtlib (DDMPXY" + curntThread_ + ") AUT(*ALL)");
      try 
      {
         output_.println("     Creating DDM libraries..." + "(t" + curntThread_ + ")");

         if (ddmlib_.run() != true) 
         {
            // If crtlib failed, check the messages
            msglist_ = ddmlib_.getMessageList();
            if (msglist_[0].getID().equals("CPF2111")) 
            {
               //do nothing (ignore)
            }
            else
            {   
               output_.println("       Setup Failed."  + "(t" + curntThread_ + ")");
               
               if (Trace.isTraceOn())
                  Trace.log(Trace.ERROR, msglist_[0].getID() + " " + msglist_[0].getText());
            }
         }

         f2_ = new KeyedFile(sys2_, "/QSYS.LIB/DDMPXY" + curntThread_ + ".LIB/KEYSRC.FILE/MBR1.MBR");
         f2_.create(new DDMKeyFormat(sys2_), "One field, CHAR(10), one key");
         
         // Create an array of records to write to the files
         records_ = new Record[9];
         RecordFormat rf = f2_.getRecordFormat();
         for (short i = 1; i < 10; ++i)
         {
            records_[i-1] = rf.getNewRecord();
            records_[i-1].setField(0, "RECORD " + String.valueOf(i) + "  ");
            // "  " was added to pad out to 10 characters to facilitate any
            // subsequent compares to records_[x]. CRS
         }

         f2_.open(AS400File.WRITE_ONLY, 9, AS400File.COMMIT_LOCK_LEVEL_NONE);
         f2_.write(records_);
         f2_.close();
         
      }
      catch(Exception e) 
      {
         output_.println("      Exception during setup." + "(t" + curntThread_ + ")");

         if (Trace.isTraceOn())
            Trace.log(Trace.ERROR, e);
      }
   }


/**
   Starts the current thread_
 **/

   public void start() 
   {
      if (thread_ == null) 
      {
         thread_ = new Thread(this);
      }
      isRunning_ = true;
      thread_.start();
   }


/**
   Stops the current thread_
 **/
   @SuppressWarnings("deprecation")
  public void stop() 
   {
      if ((thread_ != null) && thread_.isAlive()) 
      {
         thread_.stop();
      }
      thread_ = null;
      isRunning_ = false;
   }
   
   
}


// Record Format Class
class DDMKeyFormat extends RecordFormat 
{
  private static final long serialVersionUID = 1L;
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "DDMProxyStressTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.ProxyStressTest.main(newArgs); 
   }
   DDMKeyFormat(AS400 sys)
   {
      super("KEYFMT");
      addFieldDescription(new CharacterFieldDescription(new AS400Text(10, sys.getCcsid(), sys), "field1"));
      addKeyFieldDescription("field1");
   }
}


