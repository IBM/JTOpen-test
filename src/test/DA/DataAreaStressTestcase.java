///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DataAreaStressTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.DA;

import java.io.PrintWriter;
import java.lang.Thread;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.DecimalDataArea;
import com.ibm.as400.access.Trace;

import test.PasswordVault;
import test.ProxyStressTest;
import test.TestDriver;

import java.math.BigDecimal;




public class DataAreaStressTestcase 
   extends ProxyStressTest implements Runnable 
{


  public static void main(String args[]) throws Exception {
    output_ = new PrintWriter(System.out); 
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "DataAreaStressTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.ProxyStressTest.main(newArgs); 
   }

// Private variables
   private Thread thread_;
   private boolean isRunning_;
   private int curntThread_;
   private CommandCall dalib_;

   AS400Message[] msglist_;
   
   DecimalDataArea da_;

   AS400 sys3_ = null; 
      

//Constructor
   public DataAreaStressTestcase(int n) 
   {
      curntThread_ = n;
      thread_ = null;
      setup();
      da_ = new DecimalDataArea(sys3_, "/QSYS.LIB/DATEST" + curntThread_ + ".LIB" +
                               "/DATEST" + curntThread_ + ".DTAARA");
      
         char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
  sys3_ = new AS400( systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);

      
   }

  /**
   Cleanup:  deletes the libraries created on the AS/400
  **/


   private void cleanup() 
   {

      try 
      {
         output_.println("     Deleting DataArea libraries..." + "(t" + curntThread_ + ")");
         CommandCall c= new CommandCall(sys3_); 
	 TestDriver.deleteLibrary(c, "DATEST"+curntThread_);

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

      DecimalDataArea da2 = new DecimalDataArea( sys3_, "/QSYS.LIB/DATEST" + curntThread_ + ".LIB" +
                                                 "/DATEST" + curntThread_ + ".DTAARA");
      
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
               
               output_.println("     Creating DataArea..." + "(t" + curntThread_ + ")");

               // Create DataArea with length of 24
               da_.create(24, 0, new BigDecimal("0.0"), " ", "*USE");
               
               output_.println("     Clearing the DataArea..." + "(t" + curntThread_ + ")");
               // Clear the DataArea
               da_.clear();

               output_.println("     Writing to DataArea..." + "(t" + curntThread_ + ")");
               // Write to the DataArea
               da_.write(new BigDecimal("1.2"));

               if (da2.getLength() == 24) 
               {
                  output_.println("   Loop #" + i + ": Successful" + "(t" + curntThread_ + ")");
               }
               else 
               {
                  output_.println("     DataArea Length Invalid");
                  output_.println("   Loop #" + i + ": FAILED" + "(t" + curntThread_ + ")");
               }

            }
            catch(Exception e) 
            {
               output_.println("       Unexpected Exception." + "(t" + curntThread_ + ")");
               output_.println("   Loop #" + i + ": FAILED" + "(t" + curntThread_ + ")");
               if (Trace.isTraceOn())
                  Trace.log(Trace.ERROR, e);
            }
            // Try to delete the data area
            try 
            {
               output_.println("     Deleting DataArea..." + "(t" + curntThread_ + ")");
               da_.delete();
               output_.println("   Loop #" + i + ": Delete Successful" + "(t" + curntThread_ + ")");
            }
            catch(Exception e) 
            {
               output_.println("       DataArea delete FAILED." + "(t" + curntThread_ + ")");
               output_.println("   Loop #" + i + ": FAILED" + "(t" + curntThread_ + ")");
               if (Trace.isTraceOn())
                  Trace.log(Trace.ERROR, e);
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
   Sets up the Library where the data area will reside
  **/

   private void setup() 
   {
      dalib_ = new CommandCall( sys3_, "QSYS/crtlib DATEST" + curntThread_);

      try 
      {
         output_.println("     Creating DataArea libraries..." + "(t" + curntThread_ + ")");

         if (dalib_.run() != true) 
         {
            // If crtlib failed, check the messages
            msglist_ = dalib_.getMessageList();
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
