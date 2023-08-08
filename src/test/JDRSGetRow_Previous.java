///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSGetRow_Previous.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test;

import com.ibm.as400.access.AS400;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;



/**
Testcase JDRSGetRow_Previous.  This tests the following
methods of the JDBC ResultSet classes:

<ul>
<li>getRow()
<li>relative()
<li>relative()
<li>isFirst() 
<li>isLast()
<li>isBeforeFirst()
<li>isAfterLast()
</ul>
**/
public class JDRSGetRow_Previous
extends JDTestcase {



    // Private data.
    private Connection          connectionNoPrefetch_;
    private Connection          connectionNoBlocking_;
    private Statement           statement_;

    private int     currentCount = 0;
    // private int     // successCount = 0;
    private int     failedCount  = 0;
    private boolean exception    = false;


/**
Constructor.
**/
    public JDRSGetRow_Previous (AS400 systemObject,
                                Hashtable namesAndVars,
                                int runMode,
                                FileOutputStream fileOutputStream,
                                
                                String password)
    {
        super (systemObject, "JDRSGetRow_Previous",
               namesAndVars, runMode, fileOutputStream,
               password);
    }



/**
Performs setup needed before running variations.

@exception Exception If an exception occurs.
**/
    protected void setup ()
    throws Exception
    {
	if (connection_ != null) connection_.close();
            connection_ = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
            connectionNoPrefetch_ = testDriver_.getConnection (baseURL_ + ";prefetch=false", userId_, encryptedPassword_);
            connectionNoBlocking_ = testDriver_.getConnection (baseURL_ + ";block criteria=0", userId_, encryptedPassword_);
            statement_  = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                                                      ResultSet.CONCUR_UPDATABLE);
    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
    throws Exception
    {
            statement_.close ();
            connection_.close ();
            connectionNoPrefetch_.close();
            connectionNoBlocking_.close(); 
    }

    protected void cleanupConnection ()
    throws Exception
    {
            statement_.close ();
            connection_.close ();
            connectionNoPrefetch_.close();
            connectionNoBlocking_.close(); 
    }

                                                                        
    // MaxRows() set, but set to a number greater than the number of rows in the RS.                                                                        
    public void Var001 ()
    {
        if (checkJdbc20 ()) 
        {                       
           try
           {   
              Statement statement2_ = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                                   ResultSet.CONCUR_READ_ONLY);
                                                                
              statement2_.setMaxRows (JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT + 20);
        
              currentCount = 0;
              // successCount = 0;
              failedCount  = 0;    
              exception    = false;

              ResultSet rs = statement2_.executeQuery("SELECT * FROM " + JDRSGetRowTest.RSTEST_GETROW);
        
              method1(rs, JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT);
              method2(rs, JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT);
              method3(rs, JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT);
              method4(rs, JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT);
              method5(rs, JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT);        
              
              rs.close();                                                  
              statement2_.close();
           } 
           catch (SQLException e) 
           {
               System.out.println("SQLException exception: ");
               System.out.println("Message:....." + e.getMessage());
               System.out.println("SQLState:...." + e.getSQLState());
               System.out.println("Vendor Code:." + e.getErrorCode());
               System.out.println("-----------------------------------------------------");
               e.printStackTrace();
               exception = true;
           }
           catch (Exception ex) 
           {
               ex.printStackTrace();
               exception = true;
           }                      
                                    
           if ((failedCount == 0) && (exception == false))          
              succeeded();
           else
              failed();
        }
    }

    // MaxRows() set, but set to a number greater than the number of rows in the RS.                                                                        
    public void Var002 ()
    {
        if (checkJdbc20 ()) 
        {                       
           try
           {   
              Statement statement2_ = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                                   ResultSet.CONCUR_READ_ONLY);
                                                                
              statement2_.setMaxRows (JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT + 20);
        
              currentCount = 0;
              // successCount = 0;
              failedCount  = 0;    
              exception    = false;

              ResultSet rs = statement2_.executeQuery("SELECT * FROM " + JDRSGetRowTest.RSTEST_GETROW);
        
              method2(rs, JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT);
              
              rs.close();                                                  
              statement2_.close();
           } 
           catch (SQLException e) 
           {
               System.out.println("SQLException exception: ");
               System.out.println("Message:....." + e.getMessage());
               System.out.println("SQLState:...." + e.getSQLState());
               System.out.println("Vendor Code:." + e.getErrorCode());
               System.out.println("-----------------------------------------------------");
               e.printStackTrace();
               exception = true;
           }
           catch (Exception ex) 
           {
               ex.printStackTrace();
               exception = true;
           }                      
                                    
           if ((failedCount == 0) && (exception == false))          
              succeeded();
           else
              failed();
        }
    }

    // MaxRows() set, but set to a number greater than the number of rows in the RS.                                                                        
    public void Var003 ()
    {
        if (checkJdbc20 ()) 
        {                       
           try
           {   
              Statement statement2_ = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                                   ResultSet.CONCUR_READ_ONLY);
                                                                
              statement2_.setMaxRows (JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT + 20);
        
              currentCount = 0;
              // successCount = 0;
              failedCount  = 0;    
              exception    = false;

              ResultSet rs = statement2_.executeQuery("SELECT * FROM " + JDRSGetRowTest.RSTEST_GETROW);
        
              method3(rs, JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT);
              
              rs.close();                                                  
              statement2_.close();
           } 
           catch (SQLException e) 
           {
               System.out.println("SQLException exception: ");
               System.out.println("Message:....." + e.getMessage());
               System.out.println("SQLState:...." + e.getSQLState());
               System.out.println("Vendor Code:." + e.getErrorCode());
               System.out.println("-----------------------------------------------------");
               e.printStackTrace();
               exception = true;
           }
           catch (Exception ex) 
           {
               ex.printStackTrace();
               exception = true;
           }                      
                                    
           if ((failedCount == 0) && (exception == false))          
              succeeded();
           else
              failed();
        }
    }

    // MaxRows() set, but set to a number greater than the number of rows in the RS.                                                                        
    public void Var004 ()
    {
        if (checkJdbc20 ()) 
        {                       
           try
           {   
              Statement statement2_ = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                                   ResultSet.CONCUR_READ_ONLY);
                                                                
              statement2_.setMaxRows (JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT + 20);
        
              currentCount = 0;
              // successCount = 0;
              failedCount  = 0;    
              exception    = false;

              ResultSet rs = statement2_.executeQuery("SELECT * FROM " + JDRSGetRowTest.RSTEST_GETROW);
        
              method4(rs, JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT);
              
              rs.close();                                                  
              statement2_.close();
           } 
           catch (SQLException e) 
           {
               System.out.println("SQLException exception: ");
               System.out.println("Message:....." + e.getMessage());
               System.out.println("SQLState:...." + e.getSQLState());
               System.out.println("Vendor Code:." + e.getErrorCode());
               System.out.println("-----------------------------------------------------");
               e.printStackTrace();
               exception = true;
           }
           catch (Exception ex) 
           {
               ex.printStackTrace();
               exception = true;
           }                      
                                    
           if ((failedCount == 0) && (exception == false))          
              succeeded();
           else
              failed();
        }
    }

    // MaxRows() set, but set to a number greater than the number of rows in the RS.                                                                        
    public void Var005 ()
    {
        if (checkJdbc20 ()) 
        {                       
           try
           {   
              Statement statement2_ = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                                   ResultSet.CONCUR_READ_ONLY);
                                                                
              statement2_.setMaxRows (JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT + 20);
        
              currentCount = 0;
              // successCount = 0;
              failedCount  = 0;    
              exception    = false;

              ResultSet rs = statement2_.executeQuery("SELECT * FROM " + JDRSGetRowTest.RSTEST_GETROW);
        
              method5(rs, JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT);        
              
              rs.close();                                                  
              statement2_.close();
           } 
           catch (SQLException e) 
           {
               System.out.println("SQLException exception: ");
               System.out.println("Message:....." + e.getMessage());
               System.out.println("SQLState:...." + e.getSQLState());
               System.out.println("Vendor Code:." + e.getErrorCode());
               System.out.println("-----------------------------------------------------");
               e.printStackTrace();
               exception = true;
           }
           catch (Exception ex) 
           {
               ex.printStackTrace();
               exception = true;
           }                      
                                    
           if ((failedCount == 0) && (exception == false))          
              succeeded();
           else
              failed();
        }
    }


                                                                        
    // MaxRows() set, but set to a number greater than the number of rows in the RS
    public void Var006 ()
    {
        if (checkJdbc20 ()) 
        {                       
           try
           {   
              Statement statement2_ = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                                   ResultSet.CONCUR_READ_ONLY);
                                                                
              statement2_.setMaxRows (JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT + 1);
        
              currentCount = 0;
              // successCount = 0;
              failedCount  = 0;    
              exception    = false;

              ResultSet rs = statement2_.executeQuery("SELECT * FROM " + JDRSGetRowTest.RSTEST_GETROW);
        
              method1(rs, JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT);
              method2(rs, JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT);
              method3(rs, JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT);
              method4(rs, JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT);
              method5(rs, JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT);        
              
              rs.close();                                                  
              statement2_.close();
           } 
           catch (SQLException e) 
           {
               System.out.println("SQLException exception: ");
               System.out.println("Message:....." + e.getMessage());
               System.out.println("SQLState:...." + e.getSQLState());
               System.out.println("Vendor Code:." + e.getErrorCode());
               System.out.println("-----------------------------------------------------");
               e.printStackTrace();
               exception = true;
           }
           catch (Exception ex) 
           {
               ex.printStackTrace();
               exception = true;
           }                      
                                    
           if ((failedCount == 0) && (exception == false))          
              succeeded();
           else
              failed();
        }
    }

    // MaxRows() set, but set to a number greater than the number of rows in the RS.                                                                        
    public void Var007 ()
    {
        if (checkJdbc20 ()) 
        {                       
           try
           {   
              Statement statement2_ = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                                   ResultSet.CONCUR_READ_ONLY);
                                                                
              statement2_.setMaxRows (JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT + 1);
        
              currentCount = 0;
              // successCount = 0;
              failedCount  = 0;    
              exception    = false;

              ResultSet rs = statement2_.executeQuery("SELECT * FROM " + JDRSGetRowTest.RSTEST_GETROW);
        
              method2(rs, JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT);
              
              rs.close();                                                  
              statement2_.close();
           } 
           catch (SQLException e) 
           {
               System.out.println("SQLException exception: ");
               System.out.println("Message:....." + e.getMessage());
               System.out.println("SQLState:...." + e.getSQLState());
               System.out.println("Vendor Code:." + e.getErrorCode());
               System.out.println("-----------------------------------------------------");
               e.printStackTrace();
               exception = true;
           }
           catch (Exception ex) 
           {
               ex.printStackTrace();
               exception = true;
           }                      
                                    
           if ((failedCount == 0) && (exception == false))          
              succeeded();
           else
              failed();
        }
    }

    // MaxRows() set, but set to a number greater than the number of rows in the RS.                                                                        
    public void Var008 ()
    {
        if (checkJdbc20 ()) 
        {                       
           try
           {   
              Statement statement2_ = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                                   ResultSet.CONCUR_READ_ONLY);
                                                                
              statement2_.setMaxRows (JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT + 1);
        
              currentCount = 0;
              // successCount = 0;
              failedCount  = 0;    
              exception    = false;

              ResultSet rs = statement2_.executeQuery("SELECT * FROM " + JDRSGetRowTest.RSTEST_GETROW);
        
              method3(rs, JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT);
              
              rs.close();                                                  
              statement2_.close();
           } 
           catch (SQLException e) 
           {
               System.out.println("SQLException exception: ");
               System.out.println("Message:....." + e.getMessage());
               System.out.println("SQLState:...." + e.getSQLState());
               System.out.println("Vendor Code:." + e.getErrorCode());
               System.out.println("-----------------------------------------------------");
               e.printStackTrace();
               exception = true;
           }
           catch (Exception ex) 
           {
               ex.printStackTrace();
               exception = true;
           }                      
                                    
           if ((failedCount == 0) && (exception == false))          
              succeeded();
           else
              failed();
        }
    }

    // MaxRows() set, but set to a number greater than the number of rows in the RS.                                                                        
    public void Var009 ()
    {
        if (checkJdbc20 ()) 
        {                       
           try
           {   
              Statement statement2_ = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                                   ResultSet.CONCUR_READ_ONLY);
                                                                
              statement2_.setMaxRows (JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT + 1);
        
              currentCount = 0;
              // successCount = 0;
              failedCount  = 0;    
              exception    = false;

              ResultSet rs = statement2_.executeQuery("SELECT * FROM " + JDRSGetRowTest.RSTEST_GETROW);
        
              method4(rs, JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT);
              
              rs.close();                                                  
              statement2_.close();
           } 
           catch (SQLException e) 
           {
               System.out.println("SQLException exception: ");
               System.out.println("Message:....." + e.getMessage());
               System.out.println("SQLState:...." + e.getSQLState());
               System.out.println("Vendor Code:." + e.getErrorCode());
               System.out.println("-----------------------------------------------------");
               e.printStackTrace();
               exception = true;
           }
           catch (Exception ex) 
           {
               ex.printStackTrace();
               exception = true;
           }                      
                                    
           if ((failedCount == 0) && (exception == false))          
              succeeded();
           else
              failed();
        }
    }

    // MaxRows() set, but set to a number greater than the number of rows in the RS.                                                                        
    public void Var010 ()
    {
        if (checkJdbc20 ()) 
        {                       
           try
           {   
              Statement statement2_ = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                                   ResultSet.CONCUR_READ_ONLY);
                                                                
              statement2_.setMaxRows (JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT + 1);
        
              currentCount = 0;
              // successCount = 0;
              failedCount  = 0;    
              exception    = false;

              ResultSet rs = statement2_.executeQuery("SELECT * FROM " + JDRSGetRowTest.RSTEST_GETROW);
        
              method5(rs, JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT);        
              
              rs.close();                                                  
              statement2_.close();
           } 
           catch (SQLException e) 
           {
               System.out.println("SQLException exception: ");
               System.out.println("Message:....." + e.getMessage());
               System.out.println("SQLState:...." + e.getSQLState());
               System.out.println("Vendor Code:." + e.getErrorCode());
               System.out.println("-----------------------------------------------------");
               e.printStackTrace();
               exception = true;
           }
           catch (Exception ex) 
           {
               ex.printStackTrace();
               exception = true;
           }                      
                                    
           if ((failedCount == 0) && (exception == false))          
              succeeded();
           else
              failed();
        }
    }



                                                                        
    // MaxRows() set, but set to a number equal to the number of rows in the RS.
    public void Var011 ()
    {
        if (checkJdbc20 ()) 
        {                       
           try
           {   
              Statement statement2_ = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                                   ResultSet.CONCUR_READ_ONLY);
                                                                
              statement2_.setMaxRows (JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT);
        
              currentCount = 0;
              // successCount = 0;
              failedCount  = 0;    
              exception    = false;

              ResultSet rs = statement2_.executeQuery("SELECT * FROM " + JDRSGetRowTest.RSTEST_GETROW);
        
              method1(rs, JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT);
              method2(rs, JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT);
              method3(rs, JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT);
              method4(rs, JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT);
              method5(rs, JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT);        
              
              rs.close();                                                  
              statement2_.close();
           } 
           catch (SQLException e) 
           {
               System.out.println("SQLException exception: ");
               System.out.println("Message:....." + e.getMessage());
               System.out.println("SQLState:...." + e.getSQLState());
               System.out.println("Vendor Code:." + e.getErrorCode());
               System.out.println("-----------------------------------------------------");
               e.printStackTrace();
               exception = true;
           }
           catch (Exception ex) 
           {
               ex.printStackTrace();
               exception = true;
           }                      
                                    
           if ((failedCount == 0) && (exception == false))          
              succeeded();
           else
              failed();
        }
    }

    // MaxRows() set, but set to a number equal to the number of rows in the RS.
    public void Var012 ()
    {
        if (checkJdbc20 ()) 
        {                       
           try
           {   
              Statement statement2_ = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                                   ResultSet.CONCUR_READ_ONLY);
                                                                
              statement2_.setMaxRows (JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT);
        
              currentCount = 0;
              // successCount = 0;
              failedCount  = 0;    
              exception    = false;

              ResultSet rs = statement2_.executeQuery("SELECT * FROM " + JDRSGetRowTest.RSTEST_GETROW);
        
              method2(rs, JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT);
              
              rs.close();                                                  
              statement2_.close();
           } 
           catch (SQLException e) 
           {
               System.out.println("SQLException exception: ");
               System.out.println("Message:....." + e.getMessage());
               System.out.println("SQLState:...." + e.getSQLState());
               System.out.println("Vendor Code:." + e.getErrorCode());
               System.out.println("-----------------------------------------------------");
               e.printStackTrace();
               exception = true;
           }
           catch (Exception ex) 
           {
               ex.printStackTrace();
               exception = true;
           }                      
                                    
           if ((failedCount == 0) && (exception == false))          
              succeeded();
           else
              failed();
        }
    }

    // MaxRows() set, but set to a number equal to the number of rows in the RS.
    public void Var013 ()
    {
        if (checkJdbc20 ()) 
        {                       
           try
           {   
              Statement statement2_ = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                                   ResultSet.CONCUR_READ_ONLY);
                                                                
              statement2_.setMaxRows (JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT);
        
              currentCount = 0;
              // successCount = 0;
              failedCount  = 0;    
              exception    = false;

              ResultSet rs = statement2_.executeQuery("SELECT * FROM " + JDRSGetRowTest.RSTEST_GETROW);
        
              method3(rs, JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT);
              
              rs.close();                                                  
              statement2_.close();
           } 
           catch (SQLException e) 
           {
               System.out.println("SQLException exception: ");
               System.out.println("Message:....." + e.getMessage());
               System.out.println("SQLState:...." + e.getSQLState());
               System.out.println("Vendor Code:." + e.getErrorCode());
               System.out.println("-----------------------------------------------------");
               e.printStackTrace();
               exception = true;
           }
           catch (Exception ex) 
           {
               ex.printStackTrace();
               exception = true;
           }                      
                                    
           if ((failedCount == 0) && (exception == false))          
              succeeded();
           else
              failed();
        }
    }

    // MaxRows() set, but set to a number equal to the number of rows in the RS.
    public void Var014 ()
    {
        if (checkJdbc20 ()) 
        {                       
           try
           {   
              Statement statement2_ = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                                   ResultSet.CONCUR_READ_ONLY);
                                                                
              statement2_.setMaxRows (JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT);
        
              currentCount = 0;
              // successCount = 0;
              failedCount  = 0;    
              exception    = false;

              ResultSet rs = statement2_.executeQuery("SELECT * FROM " + JDRSGetRowTest.RSTEST_GETROW);
        
              method4(rs, JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT);
              
              rs.close();                                                  
              statement2_.close();
           } 
           catch (SQLException e) 
           {
               System.out.println("SQLException exception: ");
               System.out.println("Message:....." + e.getMessage());
               System.out.println("SQLState:...." + e.getSQLState());
               System.out.println("Vendor Code:." + e.getErrorCode());
               System.out.println("-----------------------------------------------------");
               e.printStackTrace();
               exception = true;
           }
           catch (Exception ex) 
           {
               ex.printStackTrace();
               exception = true;
           }                      
                                    
           if ((failedCount == 0) && (exception == false))          
              succeeded();
           else
              failed();
        }
    }

    // MaxRows() set, but set to a number equal to the number of rows in the RS.
    public void Var015 ()
    {
        if (checkJdbc20 ()) 
        {                       
           try
           {   
              Statement statement2_ = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                                   ResultSet.CONCUR_READ_ONLY);
                                                                
              statement2_.setMaxRows (JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT);
        
              currentCount = 0;
              // successCount = 0;
              failedCount  = 0;    
              exception    = false;

              ResultSet rs = statement2_.executeQuery("SELECT * FROM " + JDRSGetRowTest.RSTEST_GETROW);
        
              method5(rs, JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT);        
              
              rs.close();                                                  
              statement2_.close();
           } 
           catch (SQLException e) 
           {
               System.out.println("SQLException exception: ");
               System.out.println("Message:....." + e.getMessage());
               System.out.println("SQLState:...." + e.getSQLState());
               System.out.println("Vendor Code:." + e.getErrorCode());
               System.out.println("-----------------------------------------------------");
               e.printStackTrace();
               exception = true;
           }
           catch (Exception ex) 
           {
               ex.printStackTrace();
               exception = true;
           }                      
                                    
           if ((failedCount == 0) && (exception == false))          
              succeeded();
           else
              failed();
        }
    }



                                                                        
    // MaxRows() set, but set to a number less than the number of rows in the RS.
    public void Var016 ()
    {
        if (checkJdbc20 ()) 
        {                       
           try
           {   
              Statement statement2_ = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                                   ResultSet.CONCUR_READ_ONLY);
                                                                
              statement2_.setMaxRows (97);
        
              currentCount = 0;
              // successCount = 0;
              failedCount  = 0;    
              exception    = false;

              ResultSet rs = statement2_.executeQuery("SELECT * FROM " + JDRSGetRowTest.RSTEST_GETROW);
        
              method1(rs, 97);
              method2(rs, 97);
              method3(rs, 97);
              method4(rs, 97);
              method5(rs, 97);        
              
              rs.close();                                                  
              statement2_.close();
           } 
           catch (SQLException e) 
           {
               System.out.println("SQLException exception: ");
               System.out.println("Message:....." + e.getMessage());
               System.out.println("SQLState:...." + e.getSQLState());
               System.out.println("Vendor Code:." + e.getErrorCode());
               System.out.println("-----------------------------------------------------");
               e.printStackTrace();
               exception = true;
           }
           catch (Exception ex) 
           {
               ex.printStackTrace();
               exception = true;
           }                      
                                    
           if ((failedCount == 0) && (exception == false))          
              succeeded();
           else
              failed();
        }
    }

    // MaxRows() set, but set to a number less than the number of rows in the RS.
    public void Var017 ()
    {
        if (checkJdbc20 ()) 
        {                       
           try
           {   
              Statement statement2_ = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                                   ResultSet.CONCUR_READ_ONLY);
                                                                
              statement2_.setMaxRows (97);
        
              currentCount = 0;
              // successCount = 0;
              failedCount  = 0;    
              exception    = false;

              ResultSet rs = statement2_.executeQuery("SELECT * FROM " + JDRSGetRowTest.RSTEST_GETROW);
        
              method2(rs, 97);
              
              rs.close();                                                  
              statement2_.close();
           } 
           catch (SQLException e) 
           {
               System.out.println("SQLException exception: ");
               System.out.println("Message:....." + e.getMessage());
               System.out.println("SQLState:...." + e.getSQLState());
               System.out.println("Vendor Code:." + e.getErrorCode());
               System.out.println("-----------------------------------------------------");
               e.printStackTrace();
               exception = true;
           }
           catch (Exception ex) 
           {
               ex.printStackTrace();
               exception = true;
           }                      
                                    
           if ((failedCount == 0) && (exception == false))          
              succeeded();
           else
              failed();
        }
    }

    // MaxRows() set, but set to a number less than the number of rows in the RS.
    public void Var018 ()
    {
        if (checkJdbc20 ()) 
        {                       
           try
           {   
              Statement statement2_ = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                                   ResultSet.CONCUR_READ_ONLY);
                                                                
              statement2_.setMaxRows (97);
        
              currentCount = 0;
              // successCount = 0;
              failedCount  = 0;    
              exception    = false;

              ResultSet rs = statement2_.executeQuery("SELECT * FROM " + JDRSGetRowTest.RSTEST_GETROW);
        
              method3(rs, 97);
              
              rs.close();                                                  
              statement2_.close();
           } 
           catch (SQLException e) 
           {
               System.out.println("SQLException exception: ");
               System.out.println("Message:....." + e.getMessage());
               System.out.println("SQLState:...." + e.getSQLState());
               System.out.println("Vendor Code:." + e.getErrorCode());
               System.out.println("-----------------------------------------------------");
               e.printStackTrace();
               exception = true;
           }
           catch (Exception ex) 
           {
               ex.printStackTrace();
               exception = true;
           }                      
                                    
           if ((failedCount == 0) && (exception == false))          
              succeeded();
           else
              failed();
        }
    }

    // MaxRows() set, but set to a number less than the number of rows in the RS.
    public void Var019 ()
    {
        if (checkJdbc20 ()) 
        {                       
           try
           {   
              Statement statement2_ = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                                   ResultSet.CONCUR_READ_ONLY);
                                                                
              statement2_.setMaxRows (97);
        
              currentCount = 0;
              // successCount = 0;
              failedCount  = 0;    
              exception    = false;

              ResultSet rs = statement2_.executeQuery("SELECT * FROM " + JDRSGetRowTest.RSTEST_GETROW);
        
              method4(rs, 97);
              
              rs.close();                                                  
              statement2_.close();
           } 
           catch (SQLException e) 
           {
               System.out.println("SQLException exception: ");
               System.out.println("Message:....." + e.getMessage());
               System.out.println("SQLState:...." + e.getSQLState());
               System.out.println("Vendor Code:." + e.getErrorCode());
               System.out.println("-----------------------------------------------------");
               e.printStackTrace();
               exception = true;
           }
           catch (Exception ex) 
           {
               ex.printStackTrace();
               exception = true;
           }                      
                                    
           if ((failedCount == 0) && (exception == false))          
              succeeded();
           else
              failed();
        }
    }

    // MaxRows() set, but set to a number less than the number of rows in the RS.
    public void Var020 ()
    {
        if (checkJdbc20 ()) 
        {                       
           try
           {   
              Statement statement2_ = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                                   ResultSet.CONCUR_READ_ONLY);
                                                                
              statement2_.setMaxRows (97);
        
              currentCount = 0;
              // successCount = 0;
              failedCount  = 0;    
              exception    = false;

              ResultSet rs = statement2_.executeQuery("SELECT * FROM " + JDRSGetRowTest.RSTEST_GETROW);
        
              method5(rs, 97);        
              
              rs.close();                                                  
              statement2_.close();
           } 
           catch (SQLException e) 
           {
               System.out.println("SQLException exception: ");
               System.out.println("Message:....." + e.getMessage());
               System.out.println("SQLState:...." + e.getSQLState());
               System.out.println("Vendor Code:." + e.getErrorCode());
               System.out.println("-----------------------------------------------------");
               e.printStackTrace();
               exception = true;
           }
           catch (Exception ex) 
           {
               ex.printStackTrace();
               exception = true;
           }                      
                                    
           if ((failedCount == 0) && (exception == false))          
              succeeded();
           else
              failed();
        }
    }



    // Result set is empty, 
    public void Var021 ()
    {               
        if (checkJdbc20 ())
        {
           try
           {
              Statement statement2_ = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                                   ResultSet.CONCUR_READ_ONLY);

              // statement2_.setMaxRows (97);

              currentCount = 0;
              // successCount = 0;
              failedCount  = 0;
              exception    = false;

              ResultSet rs = statement2_.executeQuery("SELECT * FROM " + JDRSGetRowTest.RSTEST_GETROW + " WHERE 1 = 2 ");

              assert2(!rs.isBeforeFirst());
              assert2(!rs.isAfterLast());
              assert2(!rs.isLast());
              assert2(!rs.isFirst());
              assert2(rs.getRow() == 0);

              assert2(!rs.previous());
              assert2(!rs.isBeforeFirst());
              assert2(!rs.isAfterLast());
              assert2(!rs.isLast());
              assert2(!rs.isFirst());
              assert2(rs.getRow() == 0);

              assert2(!rs.previous());
              assert2(!rs.isBeforeFirst());
              assert2(!rs.isAfterLast());
              assert2(!rs.isLast());
              assert2(!rs.isFirst());
              assert2(rs.getRow() == 0);

              rs.close();
              statement2_.close();
           }
           catch (SQLException e)
           {
               System.out.println("SQLException exception: ");
               System.out.println("Message:....." + e.getMessage());
               System.out.println("SQLState:...." + e.getSQLState());
               System.out.println("Vendor Code:." + e.getErrorCode());
               System.out.println("-----------------------------------------------------");
               e.printStackTrace();
               exception = true;
           }
           catch (Exception ex)
           {
               ex.printStackTrace();
               exception = true;
           }

           if ((failedCount == 0) && (exception == false))
              succeeded();
           else
              failed();
        }
    }



    // Result set is empty, 
    public void Var022 ()
    {
        // Same as variation 21
        succeeded();
    }



    // Result set is empty, 
    public void Var023 ()
    {
        // Same as variation 21
        succeeded();
    }




    // Result set is empty, 
    public void Var024 ()
    {
        // Same as variation 21
        succeeded();
    }




    // Result set is empty, 
    public void Var025 ()
    {
        // Same as variation 21
        succeeded();
    }





    // Result set is empty, 
    public void Var026 ()
    {
        // Same as variation 21
        succeeded();
    }





    // Result set is empty, 
    public void Var027 ()
    {
        // Same as variation 21
        succeeded();
    }


    // Result set is empty, maxRows() is set, 
    public void Var028 ()
    {
        if (checkJdbc20 ())
        {
           try
           {
              Statement statement2_ = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                                   ResultSet.CONCUR_READ_ONLY);

              statement2_.setMaxRows (97);

              currentCount = 0;
              // successCount = 0;
              failedCount  = 0;
              exception    = false;

              ResultSet rs = statement2_.executeQuery("SELECT * FROM " + JDRSGetRowTest.RSTEST_GETROW + " WHERE 1 = 2 ");

              assert2(!rs.isBeforeFirst());
              assert2(!rs.isAfterLast());
              assert2(!rs.isLast());
              assert2(!rs.isFirst());
              assert2(rs.getRow() == 0);

              assert2(!rs.previous());
              assert2(!rs.isBeforeFirst());
              assert2(!rs.isAfterLast());
              assert2(!rs.isLast());
              assert2(!rs.isFirst());
              assert2(rs.getRow() == 0);

              assert2(!rs.previous());
              assert2(!rs.isBeforeFirst());
              assert2(!rs.isAfterLast());
              assert2(!rs.isLast());
              assert2(!rs.isFirst());
              assert2(rs.getRow() == 0);

              rs.close();
              statement2_.close();
           }
           catch (SQLException e)
           {
               System.out.println("SQLException exception: ");
               System.out.println("Message:....." + e.getMessage());
               System.out.println("SQLState:...." + e.getSQLState());
               System.out.println("Vendor Code:." + e.getErrorCode());
               System.out.println("-----------------------------------------------------");
               e.printStackTrace();
               exception = true;
           }
           catch (Exception ex)
           {
               ex.printStackTrace();
               exception = true;
           }

           if ((failedCount == 0) && (exception == false))
              succeeded();
           else
              failed();
        }
    }


    // Result set is empty, maxRows() is set, 
    public void Var029 ()
    {
        // Same as variation 28
        succeeded();
    }



    // Result set is empty, maxRows() is set, 
    public void Var030 ()
    {
        // Same as variation 28
        succeeded();
    }




    // Result set is empty, maxRows() is set, 
    public void Var031 ()
    {
        // Same as variation 28
        succeeded();
    }




    // Result set is empty, maxRows() is set, 
    public void Var032 ()
    {
        // Same as variation 28
        succeeded();
    }





    // Result set is empty, maxRows() is set, 
    public void Var033 ()
    {
        // Same as variation 28
        succeeded();
    }





    // Result set is empty, maxRows() is set, 
    public void Var034 ()
    {
        // Same as variation 28
        succeeded();
    }


                                                                        
    // Work on all the rows.                                                                        
    public void Var035 ()
    {
        if (checkJdbc20 ()) 
        {                       
           try
           {   
              Statement statement2_ = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                                   ResultSet.CONCUR_READ_ONLY);
                                                                
              currentCount = 0;
              // successCount = 0;
              failedCount  = 0;    
              exception    = false;

              ResultSet rs = statement2_.executeQuery("SELECT * FROM " + JDRSGetRowTest.RSTEST_GETROW);
        
              method1(rs, JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT);                      
              method2(rs, JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT);
              method3(rs, JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT);
              method4(rs, JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT);
              method5(rs, JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT);        
              
              rs.close();                                                  
              statement2_.close();
           } 
           catch (SQLException e) 
           {
               System.out.println("SQLException exception: ");
               System.out.println("Message:....." + e.getMessage());
               System.out.println("SQLState:...." + e.getSQLState());
               System.out.println("Vendor Code:." + e.getErrorCode());
               System.out.println("-----------------------------------------------------");
               e.printStackTrace();
               exception = true;
           }
           catch (Exception ex) 
           {
               ex.printStackTrace();
               exception = true;
           }                      
                                    
           if ((failedCount == 0) && (exception == false))          
              succeeded();
           else
              failed();
        }
    }

    // Work on all the rows.                                                                        
    public void Var036 ()
    {
        if (checkJdbc20 ()) 
        {                       
           try
           {   
              Statement statement2_ = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                                   ResultSet.CONCUR_READ_ONLY);
                                                                
        
              currentCount = 0;
              // successCount = 0;
              failedCount  = 0;    
              exception    = false;

              ResultSet rs = statement2_.executeQuery("SELECT * FROM " + JDRSGetRowTest.RSTEST_GETROW);
        
              method2(rs, JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT);
              
              rs.close();                                                  
              statement2_.close();
           } 
           catch (SQLException e) 
           {
               System.out.println("SQLException exception: ");
               System.out.println("Message:....." + e.getMessage());
               System.out.println("SQLState:...." + e.getSQLState());
               System.out.println("Vendor Code:." + e.getErrorCode());
               System.out.println("-----------------------------------------------------");
               e.printStackTrace();
               exception = true;
           }
           catch (Exception ex) 
           {
               ex.printStackTrace();
               exception = true;
           }                      
                                    
           if ((failedCount == 0) && (exception == false))          
              succeeded();
           else
              failed();
        }
    }

    // Work on all the rows.                                                                        
    public void Var037 ()
    {
        if (checkJdbc20 ()) 
        {                       
           try
           {   
              Statement statement2_ = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                                   ResultSet.CONCUR_READ_ONLY);
                                                                
       
              currentCount = 0;
              // successCount = 0;
              failedCount  = 0;    
              exception    = false;

              ResultSet rs = statement2_.executeQuery("SELECT * FROM " + JDRSGetRowTest.RSTEST_GETROW);
        
              method3(rs, JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT);
              
              rs.close();                                                  
              statement2_.close();
           } 
           catch (SQLException e) 
           {
               System.out.println("SQLException exception: ");
               System.out.println("Message:....." + e.getMessage());
               System.out.println("SQLState:...." + e.getSQLState());
               System.out.println("Vendor Code:." + e.getErrorCode());
               System.out.println("-----------------------------------------------------");
               e.printStackTrace();
               exception = true;
           }
           catch (Exception ex) 
           {
               ex.printStackTrace();
               exception = true;
           }                      
                                    
           if ((failedCount == 0) && (exception == false))          
              succeeded();
           else
              failed();
        }
    }

    // Work on all the rows.                                                                        
    public void Var038 ()
    {
        if (checkJdbc20 ()) 
        {                       
           try
           {   
              Statement statement2_ = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                                   ResultSet.CONCUR_READ_ONLY);
                                                                
        
              currentCount = 0;
              // successCount = 0;
              failedCount  = 0;    
              exception    = false;

              ResultSet rs = statement2_.executeQuery("SELECT * FROM " + JDRSGetRowTest.RSTEST_GETROW);
        
              method4(rs, JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT);
              
              rs.close();                                                  
              statement2_.close();
           } 
           catch (SQLException e) 
           {
               System.out.println("SQLException exception: ");
               System.out.println("Message:....." + e.getMessage());
               System.out.println("SQLState:...." + e.getSQLState());
               System.out.println("Vendor Code:." + e.getErrorCode());
               System.out.println("-----------------------------------------------------");
               e.printStackTrace();
               exception = true;
           }
           catch (Exception ex) 
           {
               ex.printStackTrace();
               exception = true;
           }                      
                                    
           if ((failedCount == 0) && (exception == false))          
              succeeded();
           else
              failed();
        }
    }

    // Work on all the rows.                                                                        
    public void Var039 ()
    {
        if (checkJdbc20 ()) 
        {                       
           try
           {   
              Statement statement2_ = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                                   ResultSet.CONCUR_READ_ONLY);
        
              currentCount = 0;
              // successCount = 0;
              failedCount  = 0;    
              exception    = false;

              ResultSet rs = statement2_.executeQuery("SELECT * FROM " + JDRSGetRowTest.RSTEST_GETROW);
        
              method5(rs, JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT);        
              
              rs.close();                                                  
              statement2_.close();
           } 
           catch (SQLException e) 
           {
               System.out.println("SQLException exception: ");
               System.out.println("Message:....." + e.getMessage());
               System.out.println("SQLState:...." + e.getSQLState());
               System.out.println("Vendor Code:." + e.getErrorCode());
               System.out.println("-----------------------------------------------------");
               e.printStackTrace();
               exception = true;
           }
           catch (Exception ex) 
           {
               ex.printStackTrace();
               exception = true;
           }                      
                                    
           if ((failedCount == 0) && (exception == false))          
              succeeded();
           else
              failed();
        }
    }


                                                                        
    // Work on all the rows, prefetch = false.                                                                        
    public void Var040 ()
    {
        if (checkJdbc20 ()) 
        {                       
           try
           {   
              Statement statement2_ = connectionNoPrefetch_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                                   ResultSet.CONCUR_READ_ONLY);
                                                                
              currentCount = 0;
              // successCount = 0;
              failedCount  = 0;    
              exception    = false;

              ResultSet rs = statement2_.executeQuery("SELECT * FROM " + JDRSGetRowTest.RSTEST_GETROW);
        
              method1(rs, JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT);                      
              method2(rs, JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT);
              method3(rs, JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT);
              method4(rs, JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT);
              method5(rs, JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT);        
              
              rs.close();                                                  
              statement2_.close();
           } 
           catch (SQLException e) 
           {
               System.out.println("SQLException exception: ");
               System.out.println("Message:....." + e.getMessage());
               System.out.println("SQLState:...." + e.getSQLState());
               System.out.println("Vendor Code:." + e.getErrorCode());
               System.out.println("-----------------------------------------------------");
               e.printStackTrace();
               exception = true;
           }
           catch (Exception ex) 
           {
               ex.printStackTrace();
               exception = true;
           }                      
                                    
           if ((failedCount == 0) && (exception == false))          
              succeeded();
           else
              failed();
        }
    }

    // Work on all the rows, prefetch = false.                                                                        
    public void Var041 ()
    {
        if (checkJdbc20 ()) 
        {                       
           try
           {   
              Statement statement2_ = connectionNoPrefetch_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                                   ResultSet.CONCUR_READ_ONLY);
                                                                
        
              currentCount = 0;
              // successCount = 0;
              failedCount  = 0;    
              exception    = false;

              ResultSet rs = statement2_.executeQuery("SELECT * FROM " + JDRSGetRowTest.RSTEST_GETROW);
        
              method2(rs, JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT);
              
              rs.close();                                                  
              statement2_.close();
           } 
           catch (SQLException e) 
           {
               System.out.println("SQLException exception: ");
               System.out.println("Message:....." + e.getMessage());
               System.out.println("SQLState:...." + e.getSQLState());
               System.out.println("Vendor Code:." + e.getErrorCode());
               System.out.println("-----------------------------------------------------");
               e.printStackTrace();
               exception = true;
           }
           catch (Exception ex) 
           {
               ex.printStackTrace();
               exception = true;
           }                      
                                    
           if ((failedCount == 0) && (exception == false))          
              succeeded();
           else
              failed();
        }
    }

    // Work on all the rows, prefetch = false.                                                                        
    public void Var042 ()
    {
        if (checkJdbc20 ()) 
        {                       
           try
           {   
              Statement statement2_ = connectionNoPrefetch_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                                   ResultSet.CONCUR_READ_ONLY);
                                                                
       
              currentCount = 0;
              // successCount = 0;
              failedCount  = 0;    
              exception    = false;

              ResultSet rs = statement2_.executeQuery("SELECT * FROM " + JDRSGetRowTest.RSTEST_GETROW);
        
              method3(rs, JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT);
              
              rs.close();                                                  
              statement2_.close();
           } 
           catch (SQLException e) 
           {
               System.out.println("SQLException exception: ");
               System.out.println("Message:....." + e.getMessage());
               System.out.println("SQLState:...." + e.getSQLState());
               System.out.println("Vendor Code:." + e.getErrorCode());
               System.out.println("-----------------------------------------------------");
               e.printStackTrace();
               exception = true;
           }
           catch (Exception ex) 
           {
               ex.printStackTrace();
               exception = true;
           }                      
                                    
           if ((failedCount == 0) && (exception == false))          
              succeeded();
           else
              failed();
        }
    }

    // Work on all the rows, prefetch = false.                                                                        
    public void Var043 ()
    {
        if (checkJdbc20 ()) 
        {                       
           try
           {   
              Statement statement2_ = connectionNoPrefetch_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                                   ResultSet.CONCUR_READ_ONLY);
                                                                
        
              currentCount = 0;
              // successCount = 0;
              failedCount  = 0;    
              exception    = false;

              ResultSet rs = statement2_.executeQuery("SELECT * FROM " + JDRSGetRowTest.RSTEST_GETROW);
        
              method4(rs, JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT);
              
              rs.close();                                                  
              statement2_.close();
           } 
           catch (SQLException e) 
           {
               System.out.println("SQLException exception: ");
               System.out.println("Message:....." + e.getMessage());
               System.out.println("SQLState:...." + e.getSQLState());
               System.out.println("Vendor Code:." + e.getErrorCode());
               System.out.println("-----------------------------------------------------");
               e.printStackTrace();
               exception = true;
           }
           catch (Exception ex) 
           {
               ex.printStackTrace();
               exception = true;
           }                      
                                    
           if ((failedCount == 0) && (exception == false))          
              succeeded();
           else
              failed();
        }
    }

    // Work on all the rows, prefetch = false.                                                                        
    public void Var044 ()
    {
        if (checkJdbc20 ()) 
        {                       
           try
           {   
              Statement statement2_ = connectionNoPrefetch_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                                   ResultSet.CONCUR_READ_ONLY);
        
              currentCount = 0;
              // successCount = 0;
              failedCount  = 0;    
              exception    = false;

              ResultSet rs = statement2_.executeQuery("SELECT * FROM " + JDRSGetRowTest.RSTEST_GETROW);
        
              method5(rs, JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT);        
              
              rs.close();                                                  
              statement2_.close();
           } 
           catch (SQLException e) 
           {
               System.out.println("SQLException exception: ");
               System.out.println("Message:....." + e.getMessage());
               System.out.println("SQLState:...." + e.getSQLState());
               System.out.println("Vendor Code:." + e.getErrorCode());
               System.out.println("-----------------------------------------------------");
               e.printStackTrace();
               exception = true;
           }
           catch (Exception ex) 
           {
               ex.printStackTrace();
               exception = true;
           }                      
                                    
           if ((failedCount == 0) && (exception == false))          
              succeeded();
           else
              failed();
        }
    }


                                                                        
    // Work on all the rows, blocksize = 0.                                                                        
    public void Var045 ()
    {
        if (checkJdbc20 ()) 
        {                       
           try
           {   
              Statement statement2_ = connectionNoBlocking_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                                   ResultSet.CONCUR_READ_ONLY);
                                                                
              currentCount = 0;
              // successCount = 0;
              failedCount  = 0;    
              exception    = false;

              ResultSet rs = statement2_.executeQuery("SELECT * FROM " + JDRSGetRowTest.RSTEST_GETROW);
        
              method1(rs, JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT);                      
              method2(rs, JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT);
              method3(rs, JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT);
              method4(rs, JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT);
              method5(rs, JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT);        
              
              rs.close();                                                  
              statement2_.close();
           } 
           catch (SQLException e) 
           {
               System.out.println("SQLException exception: ");
               System.out.println("Message:....." + e.getMessage());
               System.out.println("SQLState:...." + e.getSQLState());
               System.out.println("Vendor Code:." + e.getErrorCode());
               System.out.println("-----------------------------------------------------");
               e.printStackTrace();
               exception = true;
           }
           catch (Exception ex) 
           {
               ex.printStackTrace();
               exception = true;
           }                      
                                    
           if ((failedCount == 0) && (exception == false))          
              succeeded();
           else
              failed();
        }
    }

    // Work on all the rows, blocksize = 0.                                                                        
    public void Var046 ()
    {
        if (checkJdbc20 ()) 
        {                       
           try
           {   
              Statement statement2_ = connectionNoBlocking_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                                   ResultSet.CONCUR_READ_ONLY);
                                                                
        
              currentCount = 0;
              // successCount = 0;
              failedCount  = 0;    
              exception    = false;

              ResultSet rs = statement2_.executeQuery("SELECT * FROM " + JDRSGetRowTest.RSTEST_GETROW);
        
              method2(rs, JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT);
              
              rs.close();                                                  
              statement2_.close();
           } 
           catch (SQLException e) 
           {
               System.out.println("SQLException exception: ");
               System.out.println("Message:....." + e.getMessage());
               System.out.println("SQLState:...." + e.getSQLState());
               System.out.println("Vendor Code:." + e.getErrorCode());
               System.out.println("-----------------------------------------------------");
               e.printStackTrace();
               exception = true;
           }
           catch (Exception ex) 
           {
               ex.printStackTrace();
               exception = true;
           }                      
                                    
           if ((failedCount == 0) && (exception == false))          
              succeeded();
           else
              failed();
        }
    }

    // Work on all the rows, blocksize = 0.                                                                        
    public void Var047 ()
    {
        if (checkJdbc20 ()) 
        {                       
           try
           {   
              Statement statement2_ = connectionNoBlocking_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                                   ResultSet.CONCUR_READ_ONLY);
                                                                
       
              currentCount = 0;
              // successCount = 0;
              failedCount  = 0;    
              exception    = false;

              ResultSet rs = statement2_.executeQuery("SELECT * FROM " + JDRSGetRowTest.RSTEST_GETROW);
        
              method3(rs, JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT);
              
              rs.close();                                                  
              statement2_.close();
           } 
           catch (SQLException e) 
           {
               System.out.println("SQLException exception: ");
               System.out.println("Message:....." + e.getMessage());
               System.out.println("SQLState:...." + e.getSQLState());
               System.out.println("Vendor Code:." + e.getErrorCode());
               System.out.println("-----------------------------------------------------");
               e.printStackTrace();
               exception = true;
           }
           catch (Exception ex) 
           {
               ex.printStackTrace();
               exception = true;
           }                      
                                    
           if ((failedCount == 0) && (exception == false))          
              succeeded();
           else
              failed();
        }
    }

    // Work on all the rows, blocksize = 0.                                                                        
    public void Var048 ()
    {
        if (checkJdbc20 ()) 
        {                       
           try
           {   
              Statement statement2_ = connectionNoBlocking_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                                   ResultSet.CONCUR_READ_ONLY);
                                                                
        
              currentCount = 0;
              // successCount = 0;
              failedCount  = 0;    
              exception    = false;

              ResultSet rs = statement2_.executeQuery("SELECT * FROM " + JDRSGetRowTest.RSTEST_GETROW);
        
              method4(rs, JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT);
              
              rs.close();                                                  
              statement2_.close();
           } 
           catch (SQLException e) 
           {
               System.out.println("SQLException exception: ");
               System.out.println("Message:....." + e.getMessage());
               System.out.println("SQLState:...." + e.getSQLState());
               System.out.println("Vendor Code:." + e.getErrorCode());
               System.out.println("-----------------------------------------------------");
               e.printStackTrace();
               exception = true;
           }
           catch (Exception ex) 
           {
               ex.printStackTrace();
               exception = true;
           }                      
                                    
           if ((failedCount == 0) && (exception == false))          
              succeeded();
           else
              failed();
        }
    }

    // Work on all the rows, blocksize = 0.                                                                        
    public void Var049 ()
    {
        if (checkJdbc20 ()) 
        {                       
           try
           {   
              Statement statement2_ = connectionNoBlocking_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                                   ResultSet.CONCUR_READ_ONLY);
        
              currentCount = 0;
              // successCount = 0;
              failedCount  = 0;    
              exception    = false;

              ResultSet rs = statement2_.executeQuery("SELECT * FROM " + JDRSGetRowTest.RSTEST_GETROW);
        
              method5(rs, JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT);        
              
              rs.close();                                                  
              statement2_.close();
           } 
           catch (SQLException e) 
           {
               System.out.println("SQLException exception: ");
               System.out.println("Message:....." + e.getMessage());
               System.out.println("SQLState:...." + e.getSQLState());
               System.out.println("Vendor Code:." + e.getErrorCode());
               System.out.println("-----------------------------------------------------");
               e.printStackTrace();
               exception = true;
           }
           catch (Exception ex) 
           {
               ex.printStackTrace();
               exception = true;
           }                      
                                    
           if ((failedCount == 0) && (exception == false))          
              succeeded();
           else
              failed();
        }
    }






      
   // go backward from end to beginning, one row at a time, using previous
   void method1(ResultSet rs, int boundryValue)
        throws Exception 
   {                                  
      assert2(rs.isBeforeFirst());
      assert2(!rs.isAfterLast());
      assert2(!rs.isLast());
      assert2(!rs.isFirst());
      assert2(rs.getRow() == 0);
                           
      rs.afterLast();

      assert2(!rs.isBeforeFirst());
      assert2(rs.isAfterLast());
      assert2(!rs.isLast());
      assert2(!rs.isFirst());
      assert2(rs.getRow() == 0);
                           
         
      for (int i = boundryValue; i >= 1; i--) 
      {
           assert2(rs.previous());
           assert2(rs.getRow()   == i);        
           assert2(rs.getInt(1)  == i);        
   
           if (i == 1) 
           {
            assert2(!rs.isBeforeFirst());
            assert2(!rs.isAfterLast());
            assert2(!rs.isLast());
            assert2(rs.isFirst());
           } 
           else if (i == boundryValue) 
           {
               assert2(!rs.isBeforeFirst());
               assert2(!rs.isAfterLast());
               assert2(rs.isLast());
               assert2(!rs.isFirst());
           } 
           else 
           {
               assert2(!rs.isBeforeFirst());
               assert2(!rs.isAfterLast());
               assert2(!rs.isLast());
               assert2(!rs.isFirst());
           }                         
       }
   }
                                                          
   // go forward, one row at a time, from beginning to end using next.
   void method2(ResultSet rs, int boundryValue)
        throws Exception 
   {                    

      // get on the last row
      rs.beforeFirst();                                               

      for (int i = 1; i <= boundryValue; i++) 
      {
          assert2(rs.next());
          assert2(rs.getRow() == i);
          assert2(rs.getInt(1) == i);   
                 
          if (i == 1) 
          {
              assert2(!rs.isBeforeFirst());
              assert2(!rs.isAfterLast());
              assert2(!rs.isLast());
              assert2(rs.isFirst());
          } 
          else if (i == boundryValue) 
          {
              assert2(!rs.isBeforeFirst());
              assert2(!rs.isAfterLast());
              assert2(rs.isLast());
              assert2(!rs.isFirst());
          } 
          else 
          {
              assert2(!rs.isBeforeFirst());
              assert2(!rs.isAfterLast());
              assert2(!rs.isLast());
              assert2(!rs.isFirst());
          }         
      }                                              
   }
                    
   // In when testing absolute, this method goes forward using negative numbers
   // (-100, -99, -98, etc.).  Nothing new to test here.
   void method3(ResultSet rs, int boundryValue)
        throws Exception 
   {
      // for (int i = 0 - boundryValue; i <= -1; i++) 
      // {
      //     assert2(rs.relative(i));   
      //     assert2(rs.getRow() == (boundryValue + i + 1));
      //     assert2(rs.getInt(1) == (boundryValue + i + 1));
      // 
      //     if (i == -1) 
      //     {
      //         assert2(!rs.isBeforeFirst());
      //         assert2(!rs.isAfterLast());
      //         assert2(rs.isLast());
      //         assert2(!rs.isFirst());
      //     } 
      //     else if (i == 0 - boundryValue) 
      //     {
      //         assert2(!rs.isBeforeFirst());
      //         assert2(!rs.isAfterLast());
      //         assert2(!rs.isLast());
      //         assert2(rs.isFirst());
      //     } 
      //     else 
      //     {
      //         assert2(!rs.isBeforeFirst());
      //         assert2(!rs.isAfterLast());
      //         assert2(!rs.isLast());
      //         assert2(!rs.isFirst());
      //     }
      // }
   }


   // In when testing absolute, this method goes backward from the end of the 
   // result set (-1, -2, -2, etc.).  Nothing new to test here.
   void method4(ResultSet rs, int boundryValue)
        throws Exception 
   {
      //for (int i = -1; i >= 0 - boundryValue; i--) 
      //{
      //    assert2(rs.relative(i));
      //    assert2(rs.getRow() == (boundryValue + i + 1));
      //    assert2(rs.getInt(1) == (boundryValue + i + 1));
      //
      //    if (i == -1) 
      //    {
      //        assert2(!rs.isBeforeFirst());
      //        assert2(!rs.isAfterLast());
      //        assert2(rs.isLast());
      //        assert2(!rs.isFirst());
      //    } 
      //    else if (i == 0 - boundryValue) 
      //    {
      //        assert2(!rs.isBeforeFirst());
      //        assert2(!rs.isAfterLast());
      //        assert2(!rs.isLast());
      //        assert2(rs.isFirst());
      //    } 
      //    else 
      //    {
      //        assert2(!rs.isBeforeFirst());
      //        assert2(!rs.isAfterLast());
      //        assert2(!rs.isLast());
      //        assert2(!rs.isFirst());
      //    }
      //}
   }


   // Boundry conditions
   // In when testing absolute, this method goes various places
   // testing boundry conditions.  Nothing new to test here.
   void method5(ResultSet rs, int boundryValue)
        throws Exception 
   {  
      //rs.beforeFirst();
      //assert2(rs.next());
      //assert2(rs.relative(24));
      //assert2(!rs.isBeforeFirst());
      //assert2(!rs.isAfterLast());
      //assert2(!rs.isLast());
      //assert2(!rs.isFirst());
      //assert2(rs.getRow() == 25);
      //assert2(rs.getInt(1) == 25);     
      //
      //assert2(rs.relative(25));
      //assert2(!rs.isBeforeFirst());
      //assert2(!rs.isAfterLast());
      //assert2(!rs.isLast());
      //assert2(!rs.isFirst());
      //assert2(rs.getRow() == 50);     
      //assert2(rs.getInt(1) == 50);      
      //
      //assert2(rs.relative(-25));
      //assert2(!rs.isBeforeFirst());
      //assert2(!rs.isAfterLast());
      //assert2(!rs.isLast());
      //assert2(!rs.isFirst());
      //assert2(rs.getRow() == 25);   
      //assert2(rs.getInt(1) == 25);
      //
      //                      
      //// go off the end
      //assert2(rs.last());
      //assert2(!rs.relative(1));
      //assert2(!rs.isBeforeFirst());
      //assert2(rs.isAfterLast());
      //assert2(!rs.isLast());
      //assert2(!rs.isFirst());
      //assert2(rs.getRow() == 0);
      //
      //                          
      //// go before beginning
      //assert2(rs.first());
      //assert2(!rs.relative(-1));
      //assert2(rs.isBeforeFirst());
      //assert2(!rs.isAfterLast());
      //assert2(!rs.isLast());
      //assert2(!rs.isFirst());
      //assert2(rs.getRow() == 0);
   }  
      
      
      
    void assert2(boolean condition) 
    {      
        currentCount ++;
      
        if (!condition)
        {  
           failedCount ++;
           System.out.println("   Sub-variation " + currentCount + " failed");
        }
    }



}



