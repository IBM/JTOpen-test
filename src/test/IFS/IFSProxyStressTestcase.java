///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  IFSProxyStressTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.IFS;

import java.lang.Thread;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.IFSFile;
import com.ibm.as400.access.IFSRandomAccessFile;
import com.ibm.as400.access.Trace;

import test.PasswordVault;
import test.ProxyStressTest;




public class IFSProxyStressTestcase 
   extends ProxyStressTest implements Runnable 
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "IFSProxyStressTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.ProxyStressTest.main(newArgs); 
   }

// Private variables
   private Thread thread_;
   private boolean isRunning_;
   private int curntThread_;

   IFSFile aDirectory_;
   
   IFSRandomAccessFile aFile_;

   AS400 sys_ = null; 
      

//Constructor
   public IFSProxyStressTestcase(int n) 
   {
      curntThread_ = n;
      thread_ = null;

      aDirectory_ = new IFSFile(sys_, "/ToolboxProxy");

      setup();
      
         char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
   sys_ = new AS400( systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);

   }


/**
   Cleanup:  deletes the libraries created on the AS/400
 **/


   private void cleanup() 
   {
      IFSFile oldFile = new IFSFile(sys_, "/ToolboxProxy/IFSFile" + curntThread_);
      try 
      {
         System.out.println("     Deleting IFS directories..." + "(t" + curntThread_ + ")");

         oldFile.delete();
         aDirectory_.delete();
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

   public void run() 
   {
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
               
               System.out.println("     Creating IFS file..." + "(t" + curntThread_ + ")");

               // Open a file object that represents the file.
               aFile_ = new IFSRandomAccessFile(sys_, aDirectory_ + "/IFSFile" + curntThread_, "rw");
               
               // Establish the data to write.
               byte b = 123;
               
               // Write to the file 10 times at 1K
               // intervals.
               for (int j=0; j<10; j++)
               {
                  // Move the current offset.
                  aFile_.seek(j * 1024);
                  

                  System.out.println("     Writing to IFS file..." + "(t" + curntThread_ + ")");
                  
                  // Write to the file. The current
                  // offset advances by the size of
                  // the write.
                  aFile_.write(b);
               }
               
               System.out.println("     Closing IFS file..." + "(t" + curntThread_ + ")");

               // Close the file.
               aFile_.close();

               System.out.println("   Loop #" + i + ": Successful" + "(t" + curntThread_ + ")");
            }
            catch(Exception e) 
            {
               System.out.println("   Loop #" + i + ": FAILED" + "(t" + curntThread_ + ")");
               
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
      try 
      {
         System.out.println("     Creating IFS directories..." + "(t" + curntThread_ + ")");

         if (aDirectory_.mkdir()) 
         {
         }                        
         // Else the create directory failed.
         else
         {
            if (aDirectory_.exists() && aDirectory_.isDirectory())
            { }
            else
               System.out.println("       Setup Failed."  + "(t" + curntThread_ + ")");
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
