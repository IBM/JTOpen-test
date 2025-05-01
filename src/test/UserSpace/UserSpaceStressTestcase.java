///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  UserSpaceStressTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.UserSpace;

import java.lang.Thread;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.UserSpace;

import test.PasswordVault;
import test.ProxyStressTest;
import test.TestDriver;

import com.ibm.as400.access.Trace;




public class UserSpaceStressTestcase 
   extends ProxyStressTest implements Runnable {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "UserSpaceStressTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.ProxyStressTest.main(newArgs); 
   }
                           
// Private variables
   private Thread thread_;
   private boolean isRunning_;
   private int curntThread_;
   private CommandCall uslib_;

   AS400Message[] msglist_;
   
   UserSpace usp_;

   AS400 sys2_ = null;
      

//Constructor
   public UserSpaceStressTestcase(int n) {
     
        char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
   AS400 sys2_ = new AS400( systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);

      curntThread_ = n;
      thread_ = null;
      setup();
      usp_ = new UserSpace( sys2_, "/QSYS.LIB/STRESS" + curntThread_ + ".LIB/USSTRESS" + n + ".USRSPC");
           
   }


/**
   Cleanup:  deletes the libraries created on the AS/400
 **/


   private void cleanup() 
   {
       try {
	   CommandCall dlt = new CommandCall( sys2_);
	   TestDriver.deleteLibrary(dlt, "STRESS" + curntThread_);
       }
       catch(Exception e) 
       {
	   System.out.println("      Exception during cleanup." + "(t" + curntThread_ + ")");

	   if (Trace.isTraceOn())
	       Trace.log(Trace.ERROR, e);
       }
   }


/**
   Runs the current thread_
 **/

   @SuppressWarnings("deprecation")
  public void run() {

      String readString = null;
      
      int i;

      //While thread_ is still running
      while (isRunning_) 
      {
         //loop until user specified maxIterations is reached
         for (i=1; i<=maxIterations_; i++) 
         {
            try 
            {
               System.out.println("\n   Loop #: " + i + " (current thread: " + curntThread_ + ")");
               
               System.out.println("     Creating UserSpace..." + "(t" + curntThread_ + ")");

               usp_.create(10240,                 // The initial size is 10K
                          true,                  // Replace if the user space already exists
                          " ",                   // No extended attribute
                          (byte) 0x00,           // The initial value is a null
                          "Java Stress Testing", // The description of the user space
                          "*USE");               // Public has use authority to the user space

               // Use the write method to write bytes to the user space.
               System.out.println("     Writing to UserSpace..." + "(t" + curntThread_ + ")");
               usp_.write("Write this string to the user space.", 0);

               System.out.println("     Reading from UserSpace..." + "(t" + curntThread_ + ")");
               readString = usp_.read(0,36);
               if (readString.equals("Write this string to the user space.")) 
               {
                  System.out.println("   Loop #" + i + ": Read Successful" + "(t" + curntThread_ + ")");
               }
               else 
               {
                  System.out.println("     UserSpace read FAILED" + "(t" + curntThread_ + ")");
                  System.out.println("   Loop #" + i + ": FAILED" + "(t" + curntThread_ + ")");
               }

            }
            catch(Exception e) 
            {
               System.out.println("       Unexpected Exception." + "(t" + curntThread_ + ")");
               System.out.println("   Loop #" + i + ": FAILED" + "(t" + curntThread_ + ")");
               if (Trace.isTraceOn())
                  Trace.log(Trace.ERROR, e);
            }
            // Try to delete the user space
            try 
            {
               System.out.println("     Deleting UserSpace..." + "(t" + curntThread_ + ")");
               usp_.delete();
               System.out.println("   Loop #" + i + ": Delete Successful" + "(t" + curntThread_ + ")");
            }
            catch(Exception e) 
            {
               System.out.println("      UserSpace delete FAILED." + "(t" + curntThread_ + ")");
               
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
   Sets up the Library where the user space will reside
 **/

   private void setup() 
   {

      uslib_ = new CommandCall( sys2_, "crtlib STRESS" + curntThread_);

      try 
      {
         System.out.println("     Creating UserSpace libraries..." + "(t" + curntThread_ + ")");

         if (uslib_.run() != true) 
         {
            // If crtlib failed, check the messages
            msglist_ = uslib_.getMessageList();
            if (msglist_[0].getID().equals("CPF2111")) 
            {
               //do nothing (ignore)
            }
            else
            {
               System.out.println("       Setup Failed."  + "(t" + curntThread_ + ")");
               
               if (Trace.isTraceOn())
                  Trace.log(Trace.ERROR, msglist_[0].getID() + " " + msglist_[0].getText());
            }
         }
         else 
         {
            //Get the messages to ensure correct return code.
            msglist_ = uslib_.getMessageList();
            if (msglist_[0].getID().equals("CPC2102")) 
            {
               if (! msglist_[0].getText().equalsIgnoreCase("Library STRESS" + curntThread_ + " created.")) 
               {
                  System.out.println("       Setup Failed."  + "(t" + curntThread_ + ")");
                  
                  if (Trace.isTraceOn())
                     Trace.log(Trace.ERROR, msglist_[0].getID() + " " + msglist_[0].getText());
               }
            }
         }
      }
      catch(Exception e) 
      {
         System.out.println("      Exception during setup." + "(t" + curntThread_ + ")");

         if (Trace.isTraceOn())
            Trace.log(Trace.ERROR, e);
      }
      

   }


/**
   Starts the current thread_
 **/

   public void start() {

      if (thread_ == null) {
         thread_ = new Thread(this);
      }
      isRunning_ = true;
      thread_.start();
   }


/**
   Stops the current thread_
 **/
   @SuppressWarnings("deprecation")
  public void stop() {

      if ((thread_ != null) && thread_.isAlive()) {
         thread_.stop();
         
      }
      thread_ = null;
      isRunning_ = false;
   }
   
   
}
