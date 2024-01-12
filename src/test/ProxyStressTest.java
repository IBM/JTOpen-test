///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  ProxyStressTest.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCDriver;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.Trace;

import test.DDM.DDMProxyStressTestcase;
import test.IFS.IFSProxyStressTestcase;
import test.ServiceProgram.ServicePgmCallStressTestcase;
import test.UserSpace.UserSpaceStressTestcase;

import com.ibm.as400.access.CommandLineArguments;

import java.sql.*;
import java.lang.Thread;
import java.util.StringTokenizer;
import java.io.IOException;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Properties;



public class ProxyStressTest  
{
// Protected variables
   protected static String systemName_ = null;
   protected static String userId_ = null;
   protected static char[] encryptedPassword_ = null;
   protected static int maxThreads_ = 0;
   protected static int maxIterations_ = 0;
   protected static AS400 sys_;
   protected static AS400 pwrSys_;

// if using -tc option, which testcase to run
   private static String tc_ = null;

   // JDBC connection
   private static Connection connect_ = null;
   private static String pwrUid_ = null;
   private static char[] encryptedPwrPwd_ = null;
  
   
// Default Constructor
   public ProxyStressTest() 
   {
   }  


// Constructor
   public ProxyStressTest(String args[]) 
   {
      if (args.length == 0 || args.length < 10) 
      {
         System.out.println("\n");
         usage();
      }
      try 
      {   
         parseParms(args);
      }
      catch(Exception e)
      {
         System.out.println(e);
         System.exit(0);
      }
         
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      sys_ = new AS400(systemName_ , userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
      if (pwrUid_ != null && encryptedPwrPwd_ != null) {
         charPassword = PasswordVault.decryptPassword(encryptedPwrPwd_);
         pwrSys_ = new AS400(systemName_, pwrUid_, charPassword);
         PasswordVault.clearPassword(charPassword);
      } else
         System.out.println("\n-pwrSys option missing, JDBC testcases may fail.\n");
   }


   public static void main (String args[]) 
   {
      ProxyStressTest pst = new ProxyStressTest(args);
      
      for (int i=1; i<=maxThreads_; i++) 
      {
         if (tc_ == null || tc_.equals("CmdStressTestcase"))
         {  Trace.setTraceThreadOn(true);
            System.out.println("");
            System.out.println(" Cmd Thread " + i + " Starting...");
            System.out.println("");
            CmdStressTestcase t = new CmdStressTestcase(i);
            t.start();
         }
         if (tc_ == null || tc_.equals("UserSpaceStressTestcase") )
         {
            System.out.println("");
            System.out.println(" UserSpace Thread " + i + " Starting...");
            System.out.println("");
            UserSpaceStressTestcase t1 = new UserSpaceStressTestcase(i);
            t1.start();
         }
         if (tc_ == null || tc_.equals("DataAreaStressTestcase") )
         {   
            System.out.println("");
            System.out.println(" DataArea Thread " + i + " Starting...");
            System.out.println("");
            DataAreaStressTestcase t2 = new DataAreaStressTestcase(i);
            t2.start();
         }
         if (tc_ == null || tc_.equals("IFSProxyStressTestcase") )
         {   
            System.out.println("");
            System.out.println(" IFS Thread " + i + " Starting...");
            System.out.println("");
            IFSProxyStressTestcase t3 = new IFSProxyStressTestcase(i);
            t3.start();
         }
         if (tc_ == null || tc_.equals("ServicePgmCallStressTestcase") )
         {
            System.out.println("");
            System.out.println(" ServicePgmCall Thread " + i + " Starting...");
            System.out.println("");
            ServicePgmCallStressTestcase t4 = new ServicePgmCallStressTestcase(i);
            t4.start();
         }
         if (tc_ == null || tc_.equals("DDMProxyStressTestcase") )
         {   
            System.out.println("");
            System.out.println(" RecordLevelAccess Thread " + i + " Starting...");
            System.out.println("");
            DDMProxyStressTestcase t5 = new DDMProxyStressTestcase(i);
            t5.start();
         }
         if (tc_ == null || tc_.equals("JDBCProxyStressTestcase") )
         {
            System.out.println("");
            System.out.println(" JDBC Thread " + i + " Starting...");
            if (tc_ == null)
               System.out.println("");

            try
            {   
               // Load the AS/400 Toolbox for Java JDBC driver.
               AS400JDBCDriver driver = new com.ibm.as400.access.AS400JDBCDriver();
               
               String collection = new String("PxyCol");
               // Get a connection to the database.  Since we do not
               // provide a user id or password, a prompt will appear.
               //
               // Note that we provide a default schema here so
               // that we do not need to qualify the table name in
               // SQL statements.
               //
               char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
               AS400 as400 = new AS400(systemName_, userId_, charPassword); 
               PasswordVault.clearPassword(charPassword);
               connect_ = driver.connect(as400, new Properties(), collection); 
               
            }
            catch(SQLException e)
            {
               System.out.println("ERROR: " + e.getMessage());
               if (Trace.isTraceOn())
                  Trace.log(Trace.ERROR, e);
            }
            
            JDBCProxyStressTestcase t6 = new JDBCProxyStressTestcase(i, connect_);
            t6.start();
         }

      }

   }
   
/**
   Parse out the command line arguments
 @exception  Exception  If an exception occurs.
 **/
   
   public void parseParms(String args[]) throws Exception
   {
      String s,u,p,ps,th,l,tr,tc;

      Vector v = new Vector();
      v.addElement("-system");
      v.addElement("-uid");
      v.addElement("-pwd");
      v.addElement("-pwrSys");
      v.addElement("-threads");
      v.addElement("-loop");
      v.addElement("-trace");
      v.addElement("-tc");

      Hashtable shortcuts = new Hashtable();
      shortcuts.put("-help", "-h");
      shortcuts.put("-?", "-h");
      shortcuts.put("-s", "-system");
      shortcuts.put("-u", "-uid");
      shortcuts.put("-p", "-pwd");
      shortcuts.put("-ps", "-pwrSys");
      shortcuts.put("-thread", "-threads");
      shortcuts.put("-th", "-threads");
      shortcuts.put("-l", "-loop");
      shortcuts.put("-loops", "-loop");
      shortcuts.put("-tr", "-trace");
      
      CommandLineArguments arguments = new CommandLineArguments(args, v, shortcuts);

      if(arguments.getOptionValue("-h") != null)
         usage();

      s = arguments.getOptionValue("-system");
      if (s != null)
         systemName_ = s;
      else
      {
         System.out.println("\n Missing -system parameter. \n");
         usage();
      }

      u = arguments.getOptionValue("-uid");
      if (u != null)
         userId_ = u;
      else
      {
         System.out.println("\n Missing -uid parameter. \n");
         usage();
      }

      p = arguments.getOptionValue("-pwd");
      if (p != null)
         encryptedPassword_ = PasswordVault.getEncryptedPassword(p);
      else
      {
         System.out.println(" \n Missing -pwd parameter. \n");
         usage();
      }

      ps = arguments.getOptionValue("-pwrSys");
      if (ps != null)
      {
         int count = 0;
         StringTokenizer msc = new StringTokenizer(ps, ",");
         while (msc.hasMoreTokens())
         {
            String power = msc.nextToken();
            if (count == 0)
               pwrUid_ = power;
            else {
               encryptedPwrPwd_  = PasswordVault.getEncryptedPassword(power);
            }
            count++;
         }
         count = 0;
      }

      th = arguments.getOptionValue("-threads");
      if (th != null)
         maxThreads_ = (new Integer(th)).intValue();
      else
      {
         System.out.println("\n Missing -threads parameter. \n");
         usage();
      }

      l = arguments.getOptionValue("-loop");
      if (l != null)
         maxIterations_ = (new Integer(l)).intValue();
      else
      {
         System.out.println("\n Missing -loop parameter. \n");
         usage();
      }

      tr = arguments.getOptionValue("-trace");
      if (tr != null)
      {
         Trace.setTraceOn(true);
         StringTokenizer cats = new StringTokenizer(tr, ",");
         while (cats.hasMoreTokens())
         {
            String category = cats.nextToken();
            if (category.equalsIgnoreCase("datastream"))
               Trace.setTraceDatastreamOn(true);
            else if (category.equalsIgnoreCase("diagnostic"))
               Trace.setTraceDiagnosticOn(true);
            else if (category.equalsIgnoreCase("error"))
               Trace.setTraceErrorOn(true);
            else if (category.equalsIgnoreCase("information"))
               Trace.setTraceInformationOn(true);
            else if (category.equalsIgnoreCase("jdbc"))
               java.sql.DriverManager.setLogStream(System.out);
            else if (category.equalsIgnoreCase("proxy"))
               Trace.setTraceProxyOn(true);
            else if (category.equalsIgnoreCase("warning"))
               Trace.setTraceWarningOn(true);
            else if (category.equalsIgnoreCase("conversion"))
               Trace.setTraceConversionOn(true);
            else
               System.out.println("Unknown trace category '" + category + "'.");
         }
      }

      tc = arguments.getOptionValue("-tc");
      if (tc != null)
         tc_ = tc;
   }
      
/**
   Print out the usage
 **/

   public void usage() 
   {
      System.out.println ("USAGE: java test.ProxyStressTest (-h) -system <systemName> -uid <userid> -pwd <password> " + 
                          "-threads <number> -loop <number> [-tc <testcase> ][-trace <category>]");
      System.out.println ("-h | -help | -?       help ");
      System.out.println ("-system | -s          systemName ");
      System.out.println ("-uid | -u             userid ");
      System.out.println ("-pwd | -p             password ");
      System.out.println ("-threads | -th        number of threads to start ");
      System.out.println ("-loop | -l            number of times each thread will loop");
      System.out.println ("[-tc]                 Proxy testcase name");
      System.out.println ("[-trace | -tr]        diagnostic,error,warning,information,proxy");
      System.exit(0);
   }

   
}
