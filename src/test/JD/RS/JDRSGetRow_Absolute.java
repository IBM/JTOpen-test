///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSGetRow_Absolute.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test.JD.RS;

import com.ibm.as400.access.AS400;

import test.JDTestcase;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable; import java.util.Vector;



/**
Testcase JDRSGetRow_Absolute.  This tests the following
methods of the JDBC ResultSet classes:

<ul>
<li>getRow()
<li>absolute()
<li>relative()
<li>isFirst()
<li>isLast()
<li>isBeforeFirst()
<li>isAfterLast()
</ul>
**/
public class JDRSGetRow_Absolute
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
    public JDRSGetRow_Absolute (AS400 systemObject,
                                Hashtable<String,Vector<String>> namesAndVars,
                                int runMode,
                                FileOutputStream fileOutputStream,
                                
                                String password)
    {
        super (systemObject, "JDRSGetRow_Absolute",
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
        if (isJdbc20 ())
        {
            connection_           = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
            connectionNoPrefetch_ = testDriver_.getConnection (baseURL_ + ";prefetch=false", userId_, encryptedPassword_);
            connectionNoBlocking_ = testDriver_.getConnection (baseURL_ + ";block criteria=0", userId_, encryptedPassword_);
            statement_  = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                                                      ResultSet.CONCUR_UPDATABLE);
        }
    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
    throws Exception
    {
        if (isJdbc20 ())
        {
            statement_.close ();
            connection_.close ();
            connectionNoPrefetch_.close();
                     connectionNoBlocking_.close();

        }
        
    }

    protected void cleanupConnections ()
    throws Exception
    {
        if (isJdbc20 ())
        {
            statement_.close ();
            connection_.close ();
            connectionNoPrefetch_.close();
                     connectionNoBlocking_.close();

        }
        
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
               output_.println("SQLException exception: ");
               output_.println("Message:....." + e.getMessage());
               output_.println("SQLState:...." + e.getSQLState());
               output_.println("Vendor Code:." + e.getErrorCode());
               output_.println("-----------------------------------------------------");
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
              failedCount  = 0;
              exception    = false;

              ResultSet rs = statement2_.executeQuery("SELECT * FROM " + JDRSGetRowTest.RSTEST_GETROW);

              method2(rs, JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT);

              rs.close();
              statement2_.close();
           }
           catch (SQLException e)
           {
               output_.println("SQLException exception: ");
               output_.println("Message:....." + e.getMessage());
               output_.println("SQLState:...." + e.getSQLState());
               output_.println("Vendor Code:." + e.getErrorCode());
               output_.println("-----------------------------------------------------");
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
              failedCount  = 0;
              exception    = false;

              ResultSet rs = statement2_.executeQuery("SELECT * FROM " + JDRSGetRowTest.RSTEST_GETROW);

              method3(rs, JDRSGetRowTest.RSTEST_GETROW_ROWCOUNT);

              rs.close();
              statement2_.close();
           }
           catch (SQLException e)
           {
               output_.println("SQLException exception: ");
               output_.println("Message:....." + e.getMessage());
               output_.println("SQLState:...." + e.getSQLState());
               output_.println("Vendor Code:." + e.getErrorCode());
               output_.println("-----------------------------------------------------");
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
               output_.println("SQLException exception: ");
               output_.println("Message:....." + e.getMessage());
               output_.println("SQLState:...." + e.getSQLState());
               output_.println("Vendor Code:." + e.getErrorCode());
               output_.println("-----------------------------------------------------");
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
               output_.println("SQLException exception: ");
               output_.println("Message:....." + e.getMessage());
               output_.println("SQLState:...." + e.getSQLState());
               output_.println("Vendor Code:." + e.getErrorCode());
               output_.println("-----------------------------------------------------");
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
               output_.println("SQLException exception: ");
               output_.println("Message:....." + e.getMessage());
               output_.println("SQLState:...." + e.getSQLState());
               output_.println("Vendor Code:." + e.getErrorCode());
               output_.println("-----------------------------------------------------");
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
               output_.println("SQLException exception: ");
               output_.println("Message:....." + e.getMessage());
               output_.println("SQLState:...." + e.getSQLState());
               output_.println("Vendor Code:." + e.getErrorCode());
               output_.println("-----------------------------------------------------");
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
               output_.println("SQLException exception: ");
               output_.println("Message:....." + e.getMessage());
               output_.println("SQLState:...." + e.getSQLState());
               output_.println("Vendor Code:." + e.getErrorCode());
               output_.println("-----------------------------------------------------");
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
               output_.println("SQLException exception: ");
               output_.println("Message:....." + e.getMessage());
               output_.println("SQLState:...." + e.getSQLState());
               output_.println("Vendor Code:." + e.getErrorCode());
               output_.println("-----------------------------------------------------");
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
               output_.println("SQLException exception: ");
               output_.println("Message:....." + e.getMessage());
               output_.println("SQLState:...." + e.getSQLState());
               output_.println("Vendor Code:." + e.getErrorCode());
               output_.println("-----------------------------------------------------");
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
               output_.println("SQLException exception: ");
               output_.println("Message:....." + e.getMessage());
               output_.println("SQLState:...." + e.getSQLState());
               output_.println("Vendor Code:." + e.getErrorCode());
               output_.println("-----------------------------------------------------");
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
               output_.println("SQLException exception: ");
               output_.println("Message:....." + e.getMessage());
               output_.println("SQLState:...." + e.getSQLState());
               output_.println("Vendor Code:." + e.getErrorCode());
               output_.println("-----------------------------------------------------");
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
               output_.println("SQLException exception: ");
               output_.println("Message:....." + e.getMessage());
               output_.println("SQLState:...." + e.getSQLState());
               output_.println("Vendor Code:." + e.getErrorCode());
               output_.println("-----------------------------------------------------");
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
               output_.println("SQLException exception: ");
               output_.println("Message:....." + e.getMessage());
               output_.println("SQLState:...." + e.getSQLState());
               output_.println("Vendor Code:." + e.getErrorCode());
               output_.println("-----------------------------------------------------");
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
               output_.println("SQLException exception: ");
               output_.println("Message:....." + e.getMessage());
               output_.println("SQLState:...." + e.getSQLState());
               output_.println("Vendor Code:." + e.getErrorCode());
               output_.println("-----------------------------------------------------");
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
               output_.println("SQLException exception: ");
               output_.println("Message:....." + e.getMessage());
               output_.println("SQLState:...." + e.getSQLState());
               output_.println("Vendor Code:." + e.getErrorCode());
               output_.println("-----------------------------------------------------");
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
               output_.println("SQLException exception: ");
               output_.println("Message:....." + e.getMessage());
               output_.println("SQLState:...." + e.getSQLState());
               output_.println("Vendor Code:." + e.getErrorCode());
               output_.println("-----------------------------------------------------");
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
               output_.println("SQLException exception: ");
               output_.println("Message:....." + e.getMessage());
               output_.println("SQLState:...." + e.getSQLState());
               output_.println("Vendor Code:." + e.getErrorCode());
               output_.println("-----------------------------------------------------");
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
               output_.println("SQLException exception: ");
               output_.println("Message:....." + e.getMessage());
               output_.println("SQLState:...." + e.getSQLState());
               output_.println("Vendor Code:." + e.getErrorCode());
               output_.println("-----------------------------------------------------");
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
               output_.println("SQLException exception: ");
               output_.println("Message:....." + e.getMessage());
               output_.println("SQLState:...." + e.getSQLState());
               output_.println("Vendor Code:." + e.getErrorCode());
               output_.println("-----------------------------------------------------");
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

              assert2(!rs.next());
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

              assert2(!rs.absolute(1));
              assert2(!rs.isBeforeFirst());
              assert2(!rs.isAfterLast());
              assert2(!rs.isLast());
              assert2(!rs.isFirst());
              assert2(rs.getRow() == 0);

              assert2(!rs.absolute(-1));
              assert2(!rs.isBeforeFirst());
              assert2(!rs.isAfterLast());
              assert2(!rs.isLast());
              assert2(!rs.isFirst());
              assert2(rs.getRow() == 0);

              assert2(!rs.absolute(25));
              assert2(!rs.isBeforeFirst());
              assert2(!rs.isAfterLast());
              assert2(!rs.isLast());
              assert2(!rs.isFirst());
              assert2(rs.getRow() == 0);

              assert2(!rs.absolute(-25));
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
               output_.println("SQLException exception: ");
               output_.println("Message:....." + e.getMessage());
               output_.println("SQLState:...." + e.getSQLState());
               output_.println("Vendor Code:." + e.getErrorCode());
               output_.println("-----------------------------------------------------");
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

              assert2(!rs.next());
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
               output_.println("SQLException exception: ");
               output_.println("Message:....." + e.getMessage());
               output_.println("SQLState:...." + e.getSQLState());
               output_.println("Vendor Code:." + e.getErrorCode());
               output_.println("-----------------------------------------------------");
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
    public void Var023 ()
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
               output_.println("SQLException exception: ");
               output_.println("Message:....." + e.getMessage());
               output_.println("SQLState:...." + e.getSQLState());
               output_.println("Vendor Code:." + e.getErrorCode());
               output_.println("-----------------------------------------------------");
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
    public void Var024 ()
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


              assert2(!rs.absolute(1));
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
               output_.println("SQLException exception: ");
               output_.println("Message:....." + e.getMessage());
               output_.println("SQLState:...." + e.getSQLState());
               output_.println("Vendor Code:." + e.getErrorCode());
               output_.println("-----------------------------------------------------");
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
    public void Var025 ()
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


              assert2(!rs.absolute(-1));
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
               output_.println("SQLException exception: ");
               output_.println("Message:....." + e.getMessage());
               output_.println("SQLState:...." + e.getSQLState());
               output_.println("Vendor Code:." + e.getErrorCode());
               output_.println("-----------------------------------------------------");
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
    public void Var026 ()
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


              assert2(!rs.absolute(25));
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
               output_.println("SQLException exception: ");
               output_.println("Message:....." + e.getMessage());
               output_.println("SQLState:...." + e.getSQLState());
               output_.println("Vendor Code:." + e.getErrorCode());
               output_.println("-----------------------------------------------------");
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
    public void Var027 ()
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


              assert2(!rs.absolute(-25));
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
               output_.println("SQLException exception: ");
               output_.println("Message:....." + e.getMessage());
               output_.println("SQLState:...." + e.getSQLState());
               output_.println("Vendor Code:." + e.getErrorCode());
               output_.println("-----------------------------------------------------");
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

              assert2(!rs.next());
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

              assert2(!rs.absolute(1));
              assert2(!rs.isBeforeFirst());
              assert2(!rs.isAfterLast());
              assert2(!rs.isLast());
              assert2(!rs.isFirst());
              assert2(rs.getRow() == 0);

              assert2(!rs.absolute(-1));
              assert2(!rs.isBeforeFirst());
              assert2(!rs.isAfterLast());
              assert2(!rs.isLast());
              assert2(!rs.isFirst());
              assert2(rs.getRow() == 0);

              assert2(!rs.absolute(25));
              assert2(!rs.isBeforeFirst());
              assert2(!rs.isAfterLast());
              assert2(!rs.isLast());
              assert2(!rs.isFirst());
              assert2(rs.getRow() == 0);

              assert2(!rs.absolute(-25));
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
               output_.println("SQLException exception: ");
               output_.println("Message:....." + e.getMessage());
               output_.println("SQLState:...." + e.getSQLState());
               output_.println("Vendor Code:." + e.getErrorCode());
               output_.println("-----------------------------------------------------");
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

              assert2(!rs.next());
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
               output_.println("SQLException exception: ");
               output_.println("Message:....." + e.getMessage());
               output_.println("SQLState:...." + e.getSQLState());
               output_.println("Vendor Code:." + e.getErrorCode());
               output_.println("-----------------------------------------------------");
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
    public void Var030 ()
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
               output_.println("SQLException exception: ");
               output_.println("Message:....." + e.getMessage());
               output_.println("SQLState:...." + e.getSQLState());
               output_.println("Vendor Code:." + e.getErrorCode());
               output_.println("-----------------------------------------------------");
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
    public void Var031 ()
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


              assert2(!rs.absolute(1));
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
               output_.println("SQLException exception: ");
               output_.println("Message:....." + e.getMessage());
               output_.println("SQLState:...." + e.getSQLState());
               output_.println("Vendor Code:." + e.getErrorCode());
               output_.println("-----------------------------------------------------");
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
    public void Var032 ()
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


              assert2(!rs.absolute(-1));
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
               output_.println("SQLException exception: ");
               output_.println("Message:....." + e.getMessage());
               output_.println("SQLState:...." + e.getSQLState());
               output_.println("Vendor Code:." + e.getErrorCode());
               output_.println("-----------------------------------------------------");
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
    public void Var033 ()
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


              assert2(!rs.absolute(25));
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
               output_.println("SQLException exception: ");
               output_.println("Message:....." + e.getMessage());
               output_.println("SQLState:...." + e.getSQLState());
               output_.println("Vendor Code:." + e.getErrorCode());
               output_.println("-----------------------------------------------------");
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
    public void Var034 ()
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


              assert2(!rs.absolute(-25));
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
               output_.println("SQLException exception: ");
               output_.println("Message:....." + e.getMessage());
               output_.println("SQLState:...." + e.getSQLState());
               output_.println("Vendor Code:." + e.getErrorCode());
               output_.println("-----------------------------------------------------");
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
               output_.println("SQLException exception: ");
               output_.println("Message:....." + e.getMessage());
               output_.println("SQLState:...." + e.getSQLState());
               output_.println("Vendor Code:." + e.getErrorCode());
               output_.println("-----------------------------------------------------");
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
               output_.println("SQLException exception: ");
               output_.println("Message:....." + e.getMessage());
               output_.println("SQLState:...." + e.getSQLState());
               output_.println("Vendor Code:." + e.getErrorCode());
               output_.println("-----------------------------------------------------");
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
               output_.println("SQLException exception: ");
               output_.println("Message:....." + e.getMessage());
               output_.println("SQLState:...." + e.getSQLState());
               output_.println("Vendor Code:." + e.getErrorCode());
               output_.println("-----------------------------------------------------");
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
               output_.println("SQLException exception: ");
               output_.println("Message:....." + e.getMessage());
               output_.println("SQLState:...." + e.getSQLState());
               output_.println("Vendor Code:." + e.getErrorCode());
               output_.println("-----------------------------------------------------");
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
               output_.println("SQLException exception: ");
               output_.println("Message:....." + e.getMessage());
               output_.println("SQLState:...." + e.getSQLState());
               output_.println("Vendor Code:." + e.getErrorCode());
               output_.println("-----------------------------------------------------");
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




    // Work on all the rows, prefetch is false
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
               output_.println("SQLException exception: ");
               output_.println("Message:....." + e.getMessage());
               output_.println("SQLState:...." + e.getSQLState());
               output_.println("Vendor Code:." + e.getErrorCode());
               output_.println("-----------------------------------------------------");
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

    // Work on all the rows, prefetch is false
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
               output_.println("SQLException exception: ");
               output_.println("Message:....." + e.getMessage());
               output_.println("SQLState:...." + e.getSQLState());
               output_.println("Vendor Code:." + e.getErrorCode());
               output_.println("-----------------------------------------------------");
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

    // Work on all the rows, prefetch is false
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
               output_.println("SQLException exception: ");
               output_.println("Message:....." + e.getMessage());
               output_.println("SQLState:...." + e.getSQLState());
               output_.println("Vendor Code:." + e.getErrorCode());
               output_.println("-----------------------------------------------------");
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

    // Work on all the rows, prefetch is false
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
               output_.println("SQLException exception: ");
               output_.println("Message:....." + e.getMessage());
               output_.println("SQLState:...." + e.getSQLState());
               output_.println("Vendor Code:." + e.getErrorCode());
               output_.println("-----------------------------------------------------");
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

    // Work on all the rows, prefetch is false
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
               output_.println("SQLException exception: ");
               output_.println("Message:....." + e.getMessage());
               output_.println("SQLState:...." + e.getSQLState());
               output_.println("Vendor Code:." + e.getErrorCode());
               output_.println("-----------------------------------------------------");
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





    // Work on all the rows, block size is 0
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
               output_.println("SQLException exception: ");
               output_.println("Message:....." + e.getMessage());
               output_.println("SQLState:...." + e.getSQLState());
               output_.println("Vendor Code:." + e.getErrorCode());
               output_.println("-----------------------------------------------------");
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

    // Work on all the rows, block size is 0
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
               output_.println("SQLException exception: ");
               output_.println("Message:....." + e.getMessage());
               output_.println("SQLState:...." + e.getSQLState());
               output_.println("Vendor Code:." + e.getErrorCode());
               output_.println("-----------------------------------------------------");
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

    // Work on all the rows, block size is 0
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
               output_.println("SQLException exception: ");
               output_.println("Message:....." + e.getMessage());
               output_.println("SQLState:...." + e.getSQLState());
               output_.println("Vendor Code:." + e.getErrorCode());
               output_.println("-----------------------------------------------------");
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

    // Work on all the rows, block size is 0
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
               output_.println("SQLException exception: ");
               output_.println("Message:....." + e.getMessage());
               output_.println("SQLState:...." + e.getSQLState());
               output_.println("Vendor Code:." + e.getErrorCode());
               output_.println("-----------------------------------------------------");
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

    // Work on all the rows, block size is 0
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
               output_.println("SQLException exception: ");
               output_.println("Message:....." + e.getMessage());
               output_.println("SQLState:...." + e.getSQLState());
               output_.println("Vendor Code:." + e.getErrorCode());
               output_.println("-----------------------------------------------------");
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







   void method1(ResultSet rs, int boundryValue)
        throws Exception
   {
      assert2(rs.isBeforeFirst());
      assert2(!rs.isAfterLast());
      assert2(!rs.isLast());
      assert2(!rs.isFirst());
      assert2(rs.getRow() == 0);


      for (int i = 1; i <= boundryValue; i++)
      {
           assert2(rs.absolute(i));

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

   void method2(ResultSet rs, int boundryValue)
        throws Exception
   {
      for (int i = boundryValue; i >= 1; i--)
      {
          assert2(rs.absolute(i));
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

   void method3(ResultSet rs, int boundryValue)
        throws Exception
   {
      for (int i = 0 - boundryValue; i <= -1; i++)
      {
          assert2(rs.absolute(i));
          assert2(rs.getRow() == (boundryValue + i + 1));
          assert2(rs.getInt(1) == (boundryValue + i + 1));            

          if (i == -1)
          {
              assert2(!rs.isBeforeFirst());
              assert2(!rs.isAfterLast());
              assert2(rs.isLast());
              assert2(!rs.isFirst());
          }
          else if (i == 0 - boundryValue)
          {
              assert2(!rs.isBeforeFirst());
              assert2(!rs.isAfterLast());
              assert2(!rs.isLast());
           assert2(rs.isFirst());
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

   void method4(ResultSet rs, int boundryValue)
        throws Exception
   {
      for (int i = -1; i >= 0 - boundryValue; i--)
      {
          assert2(rs.absolute(i));
          assert2(rs.getRow() == (boundryValue + i + 1));
          assert2(rs.getInt(1) == (boundryValue + i + 1));

          if (i == -1)
          {
              assert2(!rs.isBeforeFirst());
              assert2(!rs.isAfterLast());
              assert2(rs.isLast());
              assert2(!rs.isFirst());
          }
          else if (i == 0 - boundryValue)
          {
              assert2(!rs.isBeforeFirst());
              assert2(!rs.isAfterLast());
              assert2(!rs.isLast());
              assert2(rs.isFirst());
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

   void method5(ResultSet rs, int boundryValue)
        throws Exception
   {

      assert2(rs.absolute(25));
      assert2(!rs.isBeforeFirst());
      assert2(!rs.isAfterLast());
      assert2(!rs.isLast());
      assert2(!rs.isFirst());
      assert2(rs.getRow() == 25);
      assert2(rs.getInt(1) == 25);

      assert2(rs.absolute(50));
      assert2(!rs.isBeforeFirst());
      assert2(!rs.isAfterLast());
      assert2(!rs.isLast());
      assert2(!rs.isFirst());
      assert2(rs.getRow() == 50);
      assert2(rs.getInt(1) == 50);

      assert2(rs.absolute(75));
      assert2(!rs.isBeforeFirst());
      assert2(!rs.isAfterLast());
      assert2(!rs.isLast());
      assert2(!rs.isFirst());
      assert2(rs.getRow() == 75);
      assert2(rs.getInt(1) == 75);

      assert2(!rs.absolute(boundryValue + 1));
      assert2(!rs.isBeforeFirst());
      assert2(rs.isAfterLast());
      assert2(!rs.isLast());
      assert2(!rs.isFirst());
      assert2(rs.getRow() == 0);


      assert2(!rs.absolute(0));
      assert2(rs.isBeforeFirst());
      assert2(!rs.isAfterLast());
      assert2(!rs.isLast());
      assert2(!rs.isFirst());
      assert2(rs.getRow() == 0);
   }



    void assert2(boolean condition)
    {
        currentCount ++;

        if (!condition)
        {
           failedCount ++;
           output_.println("   Sub-variation " + currentCount + " failed");
        }
    }



}



