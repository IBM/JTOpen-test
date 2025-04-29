///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDBCProxyStressTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test;

import java.sql.*;
import java.lang.Thread;
import java.beans.PropertyVetoException;
import com.ibm.as400.access.*;


public class JDBCProxyStressTestcase 
   extends ProxyStressTest implements Runnable 
{

// Private variables
   private Thread thread_;
   private boolean isRunning_;
   private int curntThread_;
   private String collectionName_ = new String("PxyCol");

   private Connection connection_   = null;

   AS400 sys2_ = null; 
   
   // Strings to be added in the WORD column of the table.
   private static final String words[]
      = { "One",      "Two",      "Three",    "Four",     "Five",
          "Six",      "Seven",    "Eight",    "Nine",     "Ten"  };
      

//Constructor
   public JDBCProxyStressTestcase(int n, Connection connect) 
   {
      curntThread_ = n;
      connection_ = connect;
      thread_ = null;

      char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
    sys2_ = new AS400( systemName_, userId_, charPassword);
   PasswordVault.clearPassword(charPassword);

   }

/**
  Clean up 
 **/
   private void cleanup(Connection connection, String tableName)
   {
      // Drop the table
      try 
      {
         System.out.println("     Dropping JDBC Table..." + "(t" + curntThread_ + ")");

         Statement dropTable = connection.createStatement ();
         dropTable.executeUpdate ("DROP TABLE " + tableName);
      }
      catch (SQLException e) 
      {
         System.out.println("      Exception during cleanup." + "(t" + curntThread_ + ")");

         if (Trace.isTraceOn())
            Trace.log(Trace.ERROR, e);
      }
   }

   
/**
  Populate a JDBC Table
 **/
   private void populate(Connection connection, String tableName, int loop)
   {
      try 
      {
            // Drop the table if it still exists.
            try 
            {
                Statement dropTable = connection.createStatement ();
                dropTable.executeUpdate ("DROP TABLE " + tableName);
            }
            catch (SQLException e) 
            {
                // Ignore.
            }

            // Create the table.
            Statement createTable = connection.createStatement ();
            createTable.executeUpdate ("CREATE TABLE " + tableName
                + " (I INTEGER, WORD VARCHAR(20), SQUARE INTEGER, "
                + " SQUAREROOT DOUBLE)");

            // Prepare a statement for inserting rows.  Since we
            // execute this multiple times, it is best to use a
            // PreparedStatement and parameter markers.
            PreparedStatement insert = connection.prepareStatement ("INSERT INTO "
                + tableName + " (I, WORD, SQUARE, SQUAREROOT) "
                + " VALUES (?, ?, ?, ?)");

            // Populate the table.
            for (int i = 1; i <= words.length; ++i) 
            {
                insert.setInt (1, i);
                insert.setString (2, words[i-1]);
                insert.setInt (3, i*i);
                insert.setDouble (4, Math.sqrt(i));
                insert.executeUpdate ();
            }

            System.out.println("   Loop #" + loop + ": Populated Successfully" + "(t" + curntThread_ + ")");

            // Output a completion message.
            if (Trace.isTraceOn())
               Trace.log(Trace.INFORMATION, "Table " + collectionName_ + "." + tableName
                         + " has been populated.");
        }
        catch (Exception e) 
        {
            System.out.println("       Unexpected Exception." + "(t" + curntThread_ + ")");
            if (Trace.isTraceOn())
               Trace.log(Trace.ERROR, e);
        }
   }
 

/**
  Query the Table you populated
 **/
   private void queryTable(Connection connection, String tableName, int loop)
   {
      try 
      {   
         DatabaseMetaData dmd = connection.getMetaData ();
         
         // Execute the query.
         Statement select = connection.createStatement ();
         ResultSet rs = select.executeQuery ("SELECT * FROM "
                                             + collectionName_ + 
                                             dmd.getCatalogSeparator() + tableName);
         
         // Get information about the result set.  Set the column
         // width to whichever is longer: the length of the label
         // or the length of the data.
         ResultSetMetaData rsmd = rs.getMetaData ();
         int columnCount = rsmd.getColumnCount ();
         String headings[] = { "I", "WORD", "SQUARE", "SQUAREROOT" };
         for (int i = 0; i < columnCount; ++i) 
         {
            //validate column headings
            if (!(headings[i].equals(rsmd.getColumnLabel(i+1))))
               throw new SQLException("Column Headings do not match.");
         }
         
         // keep track of which row we are on
         int row = 1;

         // Iterate throught the rows in the result set and 
         // validate the columns for each row.
         while (rs.next ()) 
         {  

            for (int i = 1; i <= columnCount; ++i) 
            {
               if (i == 1)
               { 
                  int num = rs.getInt(i); 
                  if (num != row)
                      throw new SQLException("Incorrect Column 1 Value."); 
               }
               else if (i == 2) 
               {
                  String value = rs.getString (i);
                  if (!(value.equals(words[row-1])))
                     throw new SQLException("Incorrect Column 2 Value.");
               }
               else if (i == 3) 
               {  
                  int sqr = rs.getInt(i);
                  if (sqr != (row*row))
                     throw new SQLException("Incorrect Column 3 Value.");
               }
               else if (i == 4) 
               {  
                  double dbl = rs.getDouble(i);
                  if (dbl != Math.sqrt(row)) 
                     throw new SQLException("Incorrect Column 4 Value.");
               }
            }
            // add 1 to the row count
            row++;
         }

         System.out.println("   Loop #" + loop + ": Query Successful" + "(t" + curntThread_ + ")");

         //close the result set
         rs.close();
      }
      catch(SQLException e)
      {
         System.out.println("       Unexpected Exception." + "(t" + curntThread_ + ")");
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
      int i;
      
      //setup the jdbc collection
      setup(connection_);
      
      //While thread_ is still running
      while (isRunning_) 
      {
         //loop until user specified maxIterations is reached
         for (i=1; i<=maxIterations_; i++) 
         {  
            System.out.println("\n   Loop #: " + i + " (current thread: " + curntThread_ + ")");
            
            String table = new String("PxTb" + curntThread_ + i);
            
            System.out.println("     Populating table..." + "(t" + curntThread_ + ")");

            populate(connection_, table, i);

            System.out.println("     Querying table..." + "(t" + curntThread_ + ")");

            queryTable(connection_, table, i);

            // drop the table before the next loop
            cleanup(connection_, table);
            
            if (i == maxIterations_)
            {
               // Close the connection_
               try 
               {
                  if (connection_ != null)
                     connection_.close ();
               }
               catch (SQLException e) 
               {
                  // Ignore.
               }

               thread_.stop();
            }
         }
      }
   }

/**
  Setup the JDBC Collection
 **/
   private void setup(Connection connection_)
   {
      // Create the collection.
      try 
      {
         System.out.println("     Creating JDBC Collection..." + "(t" + curntThread_ + ")");

         Statement s = connection_.createStatement ();

         s.executeUpdate ("CREATE COLLECTION " + collectionName_);

         s.close ();
      }
      catch (SQLException e) 
      {
         // The collection already exists.
         if(Trace.isTraceOn())
            Trace.log(Trace.INFORMATION, "The collection already exists.  " + e);
      }

      
      // We need to grant object authority to the
      // collection and the collection's journal so that
      // other users may create tables in it.
      try                                                                        //$B1A
      {
         CommandCall cmd = new CommandCall (pwrSys_);
         cmd.run ("GRTOBJAUT OBJ(QSYS/" + collectionName_ + ") OBJTYPE(*LIB) "
                  + "USER(*PUBLIC) AUT(*ALL)");
         cmd.run ("GRTOBJAUT OBJ(" + collectionName_ + "/QSQJRN) OBJTYPE(*JRN) "
                  + "USER(*PUBLIC) AUT(*ALL)");
      }
      catch(PropertyVetoException pv)
      { /* do nothing */ }
      catch(Exception e)
      {
         if (Trace.isTraceOn())
            Trace.log(Trace.INFORMATION, "Granting object authority to collection failed.", e);
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
