///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDSavepointsTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDSavepointsTestcase.java
//
// Classes:      JDSavepointsTestcase
//
////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.AS400;

import java.io.FileOutputStream;
import java.sql.*;
import java.util.*;



/**
Testcase JDSavepointsTestcase. This tests the following
methods of the JDBC SavePoints class:

<ul>
<li>setSavepoint(), rollback(), releaseSavePoint(), getSavepointId(), getSavepointName(), commit()
</ul>
**/
public class JDSavepointsTestcase
extends JDTestcase
{



    // Private data.
    private Connection          connection2_; 
    private Statement           stmt;

/**
Constructor.
**/
    public JDSavepointsTestcase (AS400 systemObject,
                         Hashtable<String,Vector<String>> namesAndVars,
                         int runMode,
                         FileOutputStream fileOutputStream,
                         
                         String password)
    {
        super (systemObject, "JDSavepointsTestcase",
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
        if(isJdbc20 ())
        {
            connection_ = testDriver_.getConnection (baseURL_ + ";data truncation=true", userId_, encryptedPassword_);
            connection2_ = testDriver_.getConnection (baseURL_ + ";data truncation=true", userId_, encryptedPassword_);

        }
    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
    throws Exception
    {
        if(isJdbc20 ())
        {
            connection_.close ();
            connection_ = null; 

        }
    }



/**
setSavepoint()
rollback()
commit()
-After inserting a new row into the table and then setting a savepoint, if you insert another row, and call rollback,
the last row you inserted should not be in the table.
**/
    public void Var001 ()
    {
        if(checkSavepointSupport())
        {
            try
            {
                //disable auto-commit
                connection_.setAutoCommit(false);
                stmt=connection_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = stmt.executeQuery("SELECT * FROM " + JDSavepointTest.SAVEPOINTTEST_GET);
                rs.last();
                int oldrows = rs.getRow();
                stmt.executeUpdate("INSERT INTO " + JDSavepointTest.SAVEPOINTTEST_GET + " (C_INTEGER) VALUES(2)");
                Savepoint svpt1=connection_.setSavepoint("SAVEPOINT_1");
                stmt.executeUpdate("INSERT INTO " + JDSavepointTest.SAVEPOINTTEST_GET + " (C_INTEGER) VALUES(3)");
                connection_.rollback(svpt1);
                connection_.commit();
                rs = stmt.executeQuery ("SELECT * FROM " + JDSavepointTest.SAVEPOINTTEST_GET);
                rs.last();
                int newrows = rs.getRow();
                rs.close();
                stmt.close();
                assertCondition ( newrows == (oldrows + 1), "newrows = "+newrows+" sb 1 + oldrows="+oldrows );
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }

        }
    } //table now contains one additional row

/**
setSavepoint()
rollback()
releaseSavepoint()
-After a Savepoint is released, attempting to access it through a rollback should throw an SQLException.
**/
    public void Var002()
    {
        if(checkSavepointSupport())
        {
            try
            {
                //disable auto-commit
                connection_.setAutoCommit(false);
                stmt=connection_.createStatement();
                Savepoint svpt1=connection_.setSavepoint("SAVEPOINT_1");
                connection_.releaseSavepoint(svpt1);
                connection_.rollback(svpt1);
                failed("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                try
                {
                    assertExceptionIsInstanceOf ( e, "java.sql.SQLException");
                    stmt.close();
                }
                catch(SQLException s)
                {
                     failed(s, "Unexpected Exception");
                }
            }
        }

    }//nothing should have been changed in the table

/**
setSavepoint()
rollback()
-After setting a save point, and then deleting a row from the table, when you rollback,
their should be no changes to the table.
**/
    public void Var003()
    {
        if(checkSavepointSupport())
        {
            try
            {
                //disable auto-commit
                connection_.setAutoCommit(false);
                stmt=connection_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs=stmt.executeQuery("SELECT * FROM " + JDSavepointTest.SAVEPOINTTEST_GET);
                rs.last();
                int oldrows = rs.getRow();
                Savepoint svpt1=connection_.setSavepoint("SAVEPOINT_1");
                stmt.executeUpdate("DELETE FROM " + JDSavepointTest.SAVEPOINTTEST_GET + " WHERE C_INTEGER=1");
                connection_.rollback(svpt1);
                connection_.commit();
                rs=stmt.executeQuery("SELECT * FROM " + JDSavepointTest.SAVEPOINTTEST_GET);
                rs.last();
                int newrows = rs.getRow();
                rs.close();
                stmt.close();
                assertCondition( oldrows == newrows);
                
            }
            catch(Exception e)
            {
                failed(e, "Unexpected Exception");
            }
        }
    } //nothing should have been added or deleted from the table

/*
setSavepoint()
rollback()
-After setting two savepoints and rolling back to the first one, trying to rollback to the second one
should throw an SQLException.
*/
    public void Var004()
    {
        if(checkSavepointSupport())
        {
            try
            {
                //disable auto-commit 
                connection_.setAutoCommit(false);
                stmt=connection_.createStatement();
                Savepoint svpt1=connection_.setSavepoint("SAVEPOINT_1");
                Savepoint svpt2=connection_.setSavepoint("SAVEPOINT_2");
                connection_.rollback(svpt1);
                connection_.rollback(svpt2);    //should throw an SQLException since it should have been released
                failed("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                try{
                    stmt.close();
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                }
                catch(SQLException s)
                {
                    failed(s, "Unexpected Exception");
                }
            }
        }
    }  //nothing should have been added or deleted from the table

/**
setSavepoint()
commit()
-After setting a savepoint and then committing it, trying to reference the savepoint should throw an SQLException
since the savepoint should have been released.
**/
    public void Var005()
    {
        if(checkSavepointSupport())
        {
            try
            {
                //disable auto-commit
                connection_.setAutoCommit(false);
                stmt=connection_.createStatement();
                Savepoint svpt1=connection_.setSavepoint("SAVEPOINT_1");
                connection_.commit();
                connection_.rollback(svpt1);    //should throw an SQLException because the savepoint should be released
                failed("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                try{
                    stmt.close();
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                }
                catch(SQLException s)
                {
                    failed(s, "Unexpected Exception");
                }
            }
        }
    } //nothing should have been added or deleted from the table

/**
releaseSavepoint()
setSavepoint()
-If you try to release a invalid save point, an SQLException should be thrown.
**/
    public void Var006()
    {
        if(checkSavepointSupport())
        {
            try
            {
                //disable auto-commit
                connection_.setAutoCommit(false);
                stmt=connection_.createStatement();
                stmt.executeUpdate("INSERT INTO " + JDSavepointTest.SAVEPOINTTEST_GET + " (C_INTEGER) VALUES(4)");
                Savepoint svpt1=connection_.setSavepoint("SAVEPOINT_1");
                connection_.rollback(svpt1);     
                connection_.commit();   //releases all savepoints
                connection_.releaseSavepoint(svpt1);   //svpt1 has been released so it is now invalid
                failed("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                try
                {
                    stmt.close();
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                }
                catch (SQLException s)
                {
                    failed(s, "Unexpected Exception");
                }
            }
        }
    } //one row should have been added to the table.  Now contains four rows (1,2,3,4).

/**
setSavepoint()
rollback()
commit()
-After setting 3 savepoints, if you rollback to the second one, all changes after the second savepoint
will be undone.
**/
    public void Var007()
    {
        if(checkSavepointSupport())
        {
            try
            {
                //disable auto-commit
                connection_.setAutoCommit(false);
                stmt=connection_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs=stmt.executeQuery("SELECT * FROM " + JDSavepointTest.SAVEPOINTTEST_GET);
                rs.last();
                int oldrows = rs.getRow();
                stmt.executeUpdate("INSERT INTO " + JDSavepointTest.SAVEPOINTTEST_GET + " (C_INTEGER) VALUES(5)");
                Savepoint svpt1=connection_.setSavepoint("SAVEPOINT_1");
                stmt.executeUpdate("INSERT INTO " + JDSavepointTest.SAVEPOINTTEST_GET + " (C_INTEGER) VALUES(6)");
                Savepoint svpt2=connection_.setSavepoint("SAVEPOINT_2");
                stmt.executeUpdate("INSERT INTO " + JDSavepointTest.SAVEPOINTTEST_GET + " (C_INTEGER) VALUES(7)");
                Savepoint svpt3=connection_.setSavepoint("SAVEPOINT_3");
                connection_.rollback(svpt2);
                connection_.commit();
                rs=stmt.executeQuery("SELECT * FROM " + JDSavepointTest.SAVEPOINTTEST_GET);
                rs.last();
                int newrows = rs.getRow();
                rs.close();
                stmt.close();
                assertCondition( newrows == (oldrows + 2), "newrows="+newrows+" sb "+(oldrows+2)+"svpt1="+svpt1+" svpt3="+svpt3 );
                
            }
            catch(Exception e)
            {
                failed(e, "Unexpected Exception");
            }
        }
    } //two rows should have been added to the table.

/**
setSavepoint()
rollback()
commit()
-After setting several savepoints and rolling back to the first, all changes after the first will be undone.
**/
    public void Var008()
    {
        if(checkSavepointSupport())
        {
            try
            {
                //disable auto-commit
                connection_.setAutoCommit(false);
                stmt=connection_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs=stmt.executeQuery("SELECT * FROM " + JDSavepointTest.SAVEPOINTTEST_GET);
                rs.last();
                int oldrows = rs.getRow();
                Savepoint svpt1=connection_.setSavepoint("SAVEPOINT_1");
                stmt.executeUpdate("INSERT INTO " + JDSavepointTest.SAVEPOINTTEST_GET + " (C_INTEGER) VALUES(21)");
                connection_.setSavepoint("SAVEPOINT_2");
                stmt.executeUpdate("INSERT INTO " + JDSavepointTest.SAVEPOINTTEST_GET + " (C_INTEGER) VALUES(33)");
                connection_.setSavepoint("SAVEPOINT_3");
                stmt.executeUpdate("DELETE FROM " + JDSavepointTest.SAVEPOINTTEST_GET + " WHERE C_INTEGER=1");
                connection_.setSavepoint("SAVEPOINT_4");
                connection_.rollback(svpt1);
                connection_.commit();
                rs=stmt.executeQuery("SELECT * FROM " + JDSavepointTest.SAVEPOINTTEST_GET);
                rs.last();
                int newrows = rs.getRow();
                rs.close();
                stmt.close();
                assertCondition( oldrows == newrows);
            }
            catch(Exception e)
            {
                failed(e, "Unexpected Exception");
            }
        }
    } //no changes should have been made to the table

/**
setSavepoint()
releaseSavepoint()
commit()
-Trying to release a Savepoint before the transaction is committed should work properly
**/
    public void Var009 ()
    {
        if(checkSavepointSupport())
        {
            try
            {
                //disable auto-commit
                connection_.setAutoCommit(false);
                stmt=connection_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs=stmt.executeQuery("SELECT * FROM " + JDSavepointTest.SAVEPOINTTEST_GET);
                rs.last();
                int oldrows = rs.getRow();
                Savepoint svpt1=connection_.setSavepoint("SAVEPOINT_1");
                stmt.executeUpdate("INSERT INTO " + JDSavepointTest.SAVEPOINTTEST_GET + " (C_INTEGER) VALUES(21)");
                connection_.releaseSavepoint(svpt1);
                stmt.executeUpdate("DELETE FROM " + JDSavepointTest.SAVEPOINTTEST_GET + " WHERE C_INTEGER=21");
                connection_.commit();
                rs=stmt.executeQuery("SELECT * FROM " + JDSavepointTest.SAVEPOINTTEST_GET);
                rs.last();
                int newrows = rs.getRow();
                rs.close();
                stmt.close();
                assertCondition( oldrows == newrows );
            }
            catch(Exception e)
            {
                failed(e, "Unexpected Exception");
            }
        }
    } //no changes should have been made to the table

/**
setSavepoint()
commit()
-Setting an unnamed savepoint should be allowed and work correctly.
**/
    public void Var010()
    {
        if(checkSavepointSupport())
        {
            try
            {
                //disable auto-commit
                connection_.setAutoCommit(false);
                stmt=connection_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs=stmt.executeQuery("SELECT * FROM " + JDSavepointTest.SAVEPOINTTEST_GET);
                rs.last();
                int oldrows = rs.getRow();
                stmt.executeUpdate("INSERT INTO " + JDSavepointTest.SAVEPOINTTEST_GET + " (C_INTEGER) VALUES(7)");
                Savepoint svpt1=connection_.setSavepoint();
                stmt.executeUpdate("INSERT INTO " + JDSavepointTest.SAVEPOINTTEST_GET + " (C_INTEGER) VALUES(8)");
                connection_.rollback(svpt1);
                connection_.commit();
                rs=stmt.executeQuery("SELECT * FROM " + JDSavepointTest.SAVEPOINTTEST_GET);
                rs.last();
                int newrows = rs.getRow();
                rs.close();
                stmt.close();
                assertCondition( newrows == (oldrows + 1));

            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    } //one row should have been added to the table.

/**
setSavepoint()
commit()
getSavepointName()
-By setting a named savepoint, getSavepointName should return the name of the savepoint.
**/
    public void Var011 ()
    {
        if(checkSavepointSupport())
        {
            try
            {
                //disable auto-commit
                connection_.setAutoCommit(false);
                Savepoint svpt1=connection_.setSavepoint("SAVEPOINT_1");
                String savepointname=svpt1.getSavepointName();
                assertCondition(savepointname.equals("SAVEPOINT_1"));
            }
            catch(Exception e)
            {
                failed(e, "Unexpected Exception");
            }
        }
    } //no changes should have been made to the table

/**
setSavepoint()
commit()
getSavepointId()
-Two unnamed savepoints should not have the same id.
**/
    public void Var012 ()
    {
        if(checkSavepointSupport())
        {
            try
            {
                //disable auto-commit
                connection_.setAutoCommit(false);
                Savepoint svpt1=connection_.setSavepoint();
                Savepoint svpt2=connection_.setSavepoint();
                assertCondition((svpt1.getSavepointId() != svpt2.getSavepointId()));
            }
            catch(Exception e)
            {
                failed(e, "Unexpected Exception");
            }
        }
    } //no changes should have been made to the table

/**
setSavepoint()
releaseSavepoint()
getSavepointName()
-If a savepoint is released, an SQLException should be thrown if you call getSavepointName.
**/
    public void Var013 ()
    {
        if(checkSavepointSupport())
        {
            try
            {
                //disable auto-commit
                connection_.setAutoCommit(false);
                Savepoint svpt1=connection_.setSavepoint("SAVEPOINT_1");
                connection_.releaseSavepoint(svpt1);
                try
                {
                    svpt1.getSavepointName();
            }
                catch (SQLException e)
                {
                    succeeded();
                    return;
                }
                //assertCondition(svpt1.getSavepointName().equals("SAVEPOINT_1"));
                if(getDriver() == JDTestDriver.DRIVER_TOOLBOX)
                    assertCondition(svpt1.getSavepointName().equals("SAVEPOINT_1"));
                else
                    failed("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                failed(e, "Unexpected Exception");
            }
        }
    } //no changes should have been made to the table

/**
setSavepoint()
releaseSavepoint()
getSavepointId()
-If a savepoint is released, an SQLException should be thrown if you call getSavepointId.
**/
    public void Var014 ()
    {
        if(checkSavepointSupport())
        {
            try
            {
                //disable auto-commit
                connection_.setAutoCommit(false);
                Savepoint svpt1=connection_.setSavepoint();
                int before = svpt1.getSavepointId();
                connection_.releaseSavepoint(svpt1);
                try
                {
                    svpt1.getSavepointName();
            }
                catch (SQLException e)
                {
                    succeeded();
                    return;
                }
                failed("Didn't throw SQLException before="+before);
                //assertCondition(svpt1.getSavepointId() == before);
            }
            catch(Exception e)
            {
                failed(e, "Unexpected Exception");
            }
        }
    } //no changes should have been made to the table

/**
setSavepoint()
releaseSavepoint()
-After releasing a savepoint and then resetting it, you should be able to rollback to the savepoint.
**/
    public void Var015 ()
    {
        if(checkSavepointSupport())
        {
            try
            {
                //disable auto-commit
                connection_.setAutoCommit(false);
                stmt=connection_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs=stmt.executeQuery("SELECT * FROM " + JDSavepointTest.SAVEPOINTTEST_GET);
                rs.last();
                int oldrows = rs.getRow();
                stmt.executeUpdate("INSERT INTO " + JDSavepointTest.SAVEPOINTTEST_GET + " (C_INTEGER) VALUES(8)");
                Savepoint svpt1=connection_.setSavepoint("SAVEPOINT_1");
                connection_.releaseSavepoint(svpt1); 
                svpt1=connection_.setSavepoint();
                connection_.rollback(svpt1);
                connection_.commit();
                rs=stmt.executeQuery("SELECT * FROM " + JDSavepointTest.SAVEPOINTTEST_GET);
                rs.last();
                int newrows = rs.getRow();
                rs.close();
                stmt.close();
                assertCondition( newrows == (oldrows + 1) );
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }

        }
    } //one row should have been inserted into the table.

/**
setSavepoint()
commit()
getSavepointId()
-After releasing a savepoint by using commit, a call to getSavepointId should throw an exception.
**/
    public void Var016 ()
    {
        if(checkSavepointSupport())
        {
            try
            {
                //disable auto-commit
                connection_.setAutoCommit(false);
                stmt=connection_.createStatement();
                stmt.executeUpdate("INSERT INTO " + JDSavepointTest.SAVEPOINTTEST_GET + " (C_INTEGER) VALUES(9)");
                Savepoint svpt1=connection_.setSavepoint();
                int before = svpt1.getSavepointId();
                connection_.commit();
                assertCondition(svpt1.getSavepointId() == before);
                try { stmt.close(); } catch (Exception e) {}
            }
            catch(Exception e)
            {
               failed (e, "Unexpected Exception");
            }
        }
    } //one row should have been added to the table.

/**
setSavepoint()
commit()
getSavepointName
-After releasing a savepoint by using commit, a call to getSavepointName 
 should throw an exception.
**/
    public void Var017 ()
    {
        if(checkSavepointSupport())
        {
            try
            {
                //disable auto-commit
                connection_.setAutoCommit(false);
                stmt=connection_.createStatement();
                Savepoint svpt1=connection_.setSavepoint("SAVEPOINT_1");
                String before = svpt1.getSavepointName();
                connection_.commit();
                assertCondition(before.equals(svpt1.getSavepointName()));
                try { stmt.close(); } catch (Exception e) {}
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    } //no changes should have been made to the table 

/**
setSavepoint()
rollback()
getSavepointName()
-If a rollback to a savepoint occurs, using getSavepointName on any savepoints after the one that is rolled back to
 should throw an exception if that savepoint was declared before the rollback.
**/
    public void Var018 ()
    {
        if(checkSavepointSupport())
        {
            try
            {
                //disable auto-commit
                connection_.setAutoCommit(false);
                stmt=connection_.createStatement();
                Savepoint svpt1=connection_.setSavepoint("SAVEPOINT_1");
                stmt.executeUpdate("INSERT INTO " + JDSavepointTest.SAVEPOINTTEST_GET + " (C_INTEGER) VALUES(333)");
                Savepoint svpt2=connection_.setSavepoint("SAVEPOINT_2");    
                String before1 = svpt1.getSavepointName();
                String before2 = svpt2.getSavepointName();
                connection_.rollback(svpt1);
                assertCondition( (before1.equals(svpt1.getSavepointName())) &&
                                 (before2.equals(svpt2.getSavepointName())));
                try { stmt.close(); } catch (Exception e) {}
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    } //no changes should have been made to the table.

/**
setSavepoint()
rollback()
getSavepointId()
-If a rollback to a savepoint occurs, using getSavepointName on any savepoints after the one that is rolled back
to should throw an exception if that savepoint was declared before the rollback.
**/
    public void Var019 ()
    {
        if(checkSavepointSupport())
        {
            try
            {
                //disable auto-commit
                connection_.setAutoCommit(false);
                stmt=connection_.createStatement();
                Savepoint svpt1=connection_.setSavepoint();
                stmt.executeUpdate("INSERT INTO " + JDSavepointTest.SAVEPOINTTEST_GET + " (C_INTEGER) VALUES(3)");
                Savepoint svpt2=connection_.setSavepoint();            
                int before1 = svpt1.getSavepointId();
                int before2 = svpt2.getSavepointId();
                connection_.rollback(svpt1);
                assertCondition( (before1 == svpt1.getSavepointId()) &&
                                 (before2 == svpt2.getSavepointId()));
                try { stmt.close(); } catch (Exception e) {}
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    } //no changes should have been made to the table

/**
setSavepoint()
rollback()
getSavepointName()
-You should be able to get the name of a savepoint you rolled back to.
**/
    public void Var020 ()
    {
        if(checkSavepointSupport())
        {
            try
            {
                //disable auto-commit
                connection_.setAutoCommit(false);
                stmt=connection_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs=stmt.executeQuery("SELECT * FROM " + JDSavepointTest.SAVEPOINTTEST_GET);
                rs.last();
                int oldrows = rs.getRow();
                stmt.executeUpdate("INSERT INTO " + JDSavepointTest.SAVEPOINTTEST_GET + " (C_INTEGER) VALUES(11)");
                Savepoint svpt1=connection_.setSavepoint("SAVEPOINT_1");
                stmt.executeUpdate("INSERT INTO " + JDSavepointTest.SAVEPOINTTEST_GET + " (C_INTEGER) VALUES(112)");
                connection_.setSavepoint("SAVEPOINT_2");
                connection_.rollback(svpt1);
                String name=svpt1.getSavepointName();
                connection_.commit();
                rs=stmt.executeQuery("SELECT * FROM " + JDSavepointTest.SAVEPOINTTEST_GET);
                rs.last();
                int newrows = rs.getRow();
                rs.close();
                stmt.close();
                assertCondition(name.equals("SAVEPOINT_1") && ( newrows == (oldrows + 1) ));
            }
            catch(Exception e)
            {
                failed(e, "Unexpected Exception");
            }

        }
    } //one row should have been added to the table.  

/**
setSavepoint()
rollback)
getSavepointId()
-You should be able to get the Id of a savepoint you rolled back to.
**/
    public void Var021 ()
    {
        if(checkSavepointSupport())
        {
            try
            {
                //disable auto-commit
                connection_.setAutoCommit(false);
                stmt=connection_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs=stmt.executeQuery("SELECT * FROM " + JDSavepointTest.SAVEPOINTTEST_GET);
                rs.last();
                int oldrows = rs.getRow();
                stmt.executeUpdate("INSERT INTO " + JDSavepointTest.SAVEPOINTTEST_GET + " (C_INTEGER) VALUES(12)");
                Savepoint svpt1=connection_.setSavepoint();
                stmt.executeUpdate("INSERT INTO " + JDSavepointTest.SAVEPOINTTEST_GET + " (C_INTEGER) VALUES(13)");
                connection_.setSavepoint();
                connection_.rollback(svpt1);
                svpt1.getSavepointId();
                connection_.commit();
                rs=stmt.executeQuery("SELECT * FROM " + JDSavepointTest.SAVEPOINTTEST_GET);
                rs.last();
                int newrows = rs.getRow();
                rs.close();
                stmt.close();
                //need to put assertCondition statment to check if id was successfully found
                assertCondition( newrows == (oldrows + 1) );
            }
            catch(Exception e)
            {
                failed(e, "Unexpected Exception");
            }
        }
    } //one row should have been added to the table.

/**
setSavepoint()
getSavepointName()
-Two savepoints should be able to have the same name.
**/
    public void Var022 ()
    {
        if(checkSavepointSupport())
        {
            try
            {
                //disable auto-commit
                connection_.setAutoCommit(false);
                stmt=connection_.createStatement();
                Savepoint svpt1=connection_.setSavepoint("SAVEPOINT_1");
                Savepoint svpt2=connection_.setSavepoint("SAVEPOINT_1");
                String name1=svpt1.getSavepointName();
                String name2=svpt2.getSavepointName();
                connection_.commit();
                stmt.close();
                assertCondition(name1.equals(name2));
            }
            catch(Exception e)
            {
                failed(e, "Unexpected Exception");
            }
        }
    } //no changes should have been made to the table

/**
setSavepoint()
rollback()
getSavepointName()
-If you roll back to a save point, you should still be able to access a savepoint that was created
before the one you rolled back to with getSavepointName().
**/
    public void Var023 ()
    {
        if(checkSavepointSupport())
        {
            try
            {
                //disable auto-commit
                connection_.setAutoCommit(false);
                stmt=connection_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs=stmt.executeQuery("SELECT * FROM " + JDSavepointTest.SAVEPOINTTEST_GET);
                rs.last();
                int oldrows = rs.getRow();
                Savepoint svpt1=connection_.setSavepoint("SAVEPOINT_1");
                stmt.executeUpdate("INSERT INTO " + JDSavepointTest.SAVEPOINTTEST_GET + " (C_INTEGER) VALUES(14)");
                Savepoint svpt2=connection_.setSavepoint("SAVEPOINT_2");
                stmt.executeUpdate("INSERT INTO " + JDSavepointTest.SAVEPOINTTEST_GET + " (C_INTEGER) VALUES(15)");
                connection_.setSavepoint("SAVEPOINT_3");
                connection_.rollback(svpt2);
                String name=svpt1.getSavepointName();
                String name2=svpt2.getSavepointName();
                connection_.commit();
                rs=stmt.executeQuery("SELECT * FROM " + JDSavepointTest.SAVEPOINTTEST_GET);
                rs.last();
                int newrows = rs.getRow();
                rs.close();
                stmt.close();
                assertCondition((name.equals("SAVEPOINT_1"))&&(name2.equals("SAVEPOINT_2"))&&( newrows == (oldrows + 1)));
            }
            catch(Exception e)
            {
                failed(e, "Unexpected Exception");
            }
        }
    } //one row should have been added to the table.

/**
setSavepoint()
rollback()
If you roll back to a save point, you should be able to roll back to a savepoint that was created before it.
**/
    public void Var024 ()
    {
        if(checkSavepointSupport())
        {
            try
            {
                //disable auto-commit
                connection_.setAutoCommit(false);
                stmt=connection_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs=stmt.executeQuery("SELECT * FROM " + JDSavepointTest.SAVEPOINTTEST_GET);
                rs.last();
                int oldrows = rs.getRow();
                Savepoint svpt1=connection_.setSavepoint();
                stmt.executeUpdate("INSERT INTO " + JDSavepointTest.SAVEPOINTTEST_GET + " (C_INTEGER) VALUES(222)");
                Savepoint svpt2=connection_.setSavepoint();
                connection_.rollback(svpt2);
                connection_.rollback(svpt1);
                connection_.commit();
                rs=stmt.executeQuery("SELECT * FROM " + JDSavepointTest.SAVEPOINTTEST_GET);
                rs.last();
                int newrows = rs.getRow();
                rs.close();
                stmt.close();
                assertCondition( oldrows == newrows );
            }
            catch(Exception e)
            {
                failed(e, "Unexpected Exception");
            }
        }
    } //no changes should have been made to the table

/**
setSavepoint()
rollback()
-If a general rollback is called, no changes should be made to the database.
**/
    public void Var025 ()
    {
        if(checkSavepointSupport())
        {
            try
            {
                //disable auto-commit
                connection_.setAutoCommit(false);
                stmt=connection_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs=stmt.executeQuery("SELECT * FROM " + JDSavepointTest.SAVEPOINTTEST_GET);
                rs.last();
                int oldrows = rs.getRow();
                stmt.executeUpdate("INSERT INTO " + JDSavepointTest.SAVEPOINTTEST_GET + " (C_INTEGER) VALUES(212)");
                connection_.setSavepoint();
                connection_.rollback();
                connection_.commit();
                rs=stmt.executeQuery("SELECT * FROM " + JDSavepointTest.SAVEPOINTTEST_GET);
                rs.last();
                int newrows = rs.getRow();
                rs.close();  
                stmt.close();
                assertCondition( oldrows == newrows );
            }
            catch(Exception e)
            {
                failed(e, "Unexpected Exception");
            }
        }
    } //no changes should have been made to the table

/**
setSavepoint()
rollback()
getSavepointName()
-If a general rollback is called, you should no longer be able to access any savepoints using getSavepointName.
 An SQLException should be thrown.
**/
    public void Var026 ()
    {
        if(checkSavepointSupport())
        {
            try
            {
                //disable auto-commit
                connection_.setAutoCommit(false);
                Savepoint svpt1=connection_.setSavepoint("SAVEPOINT_1");
                connection_.rollback();
                assertCondition(svpt1.getSavepointName().equals("SAVEPOINT_1"));
            }
            catch(Exception e)
            {
                failed(e, "Unexpected Exception");
            }
        }
    } //no changes should have been made to the table

/**
setSavepoint()
rollback()
getSavepointId()
-If a general rollback is called, you should no longer be able to access any savepoints using getSavepointId.
An SQLException should be thrown.
**/
    public void Var027 ()
    {
        if(checkSavepointSupport())
        {
            try
            {
                //disable auto-commit
                connection_.setAutoCommit(false);
                Savepoint svpt1=connection_.setSavepoint();
                int before = svpt1.getSavepointId();
                connection_.rollback();
                assertCondition(before == svpt1.getSavepointId());
            }
            catch(Exception e)
            {
                failed(e, "Unexpected Exception");
            }
        }
    } //no changes should have been made to the table


/**
setSavepoint()
autocommit()
- When autocommit is enabled, you should be able to set a savepoint.
**/
    public void Var028 ()
    {
        if(checkSavepointSupport())
        {
            try
            {
                //auto-commit enabled
                connection_.setAutoCommit(true);
                stmt=connection_.createStatement();
                stmt.executeUpdate("DELETE FROM " + JDSavepointTest.SAVEPOINTTEST_GET + " WHERE C_INTEGER=14");
                Savepoint svpt1=connection_.setSavepoint("SAVEPOINT_1");
                failed("Didn't throw SQLException but got "+svpt1);
            }
            catch(SQLException e)
            {
               String message = e.getMessage().toUpperCase();
               //
               // The native driver returns a function sequence error
               //
	       if (getDriver () == JDTestDriver.DRIVER_NATIVE) { 
		   if (message.indexOf("FUNCTION SEQUENCE") >= 0)
                  succeeded();
               else
                  failed (e, "Unexpected Exception");
	       } else {
		   succeeded(); 
            }
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }  //If 14 was in the table it should have been deleted.

/**
setSavepoint()
rollback()
autocommit enabled
-Assuming that when autocommit is enabled, when you set a savepoint and try to rollback to it
an SQLException should be thrown.
**/
    public void Var029()
    {
        if(checkSavepointSupport())
        {
            try
            {
                //auto commit enabled
                connection_.setAutoCommit(true);
                stmt=connection_.createStatement();
                Savepoint svpt1=connection_.setSavepoint();
                stmt.executeUpdate("DELETE FROM " + JDSavepointTest.SAVEPOINTTEST_GET + " WHERE C_INTEGER=13");
                connection_.rollback(svpt1);
                failed("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                try{
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                    stmt.close();
                }
                catch (SQLException s)
                {
                    failed(s, "Unexpected Exception");
                }
            }
        }
    }  //if 13 was in the table it should have been deleted.

/**
setSavepoint()
getSavepointId()
autocommit enabled
-Assuming that when autocommit is enabled that a savepoint is released after the next executable statement.
In this case, a call to getSavepointId should throw an SQLException.
**/
    public void Var030()
    {
        if(checkSavepointSupport())
        {
            try
            {
                //auto-commit enabled
                connection_.setAutoCommit(true);
                stmt=connection_.createStatement();
                Savepoint svpt1=connection_.setSavepoint();
                stmt.executeUpdate("DELETE FROM " + JDSavepointTest.SAVEPOINTTEST_GET + " WHERE C_INTEGER=12");
                svpt1.getSavepointId();
                failed("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                try {
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                    stmt.close();
                }
                catch (SQLException s)
                {
                    failed (s, "Unexpected Exception");
                }
            }
        }
    }  //if 12 was in the table, it should have been deleted.

/**
setSavepoint()
getSavepointName()
autocommit enabled
-Assuming that when autocommit is enabled that a savepoint is released after the next executable statement.
In this case, a call to getSavepointName should throw an SQLException.
**/
    public void Var031()
    {
        //this code may have to be changed once the relationship between savepoints and autocommit is decided
        if(checkSavepointSupport())
        {
            try
            {
                //auto-commit enabled
                connection_.setAutoCommit(true);
                stmt=connection_.createStatement();
                Savepoint svpt1=connection_.setSavepoint("SAVEPOINT_1");
                stmt.executeUpdate("DELETE FROM " + JDSavepointTest.SAVEPOINTTEST_GET + " WHERE C_INTEGER=11");
                svpt1.getSavepointName();
                failed("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                try{
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                    stmt.close();
                }
                catch (SQLException s)
                {
                    failed (s, "Unexpected Exception");
                }
            }
        }
    } //if 11 was in the table it should have been deleted.

/**
setSavepoint()
rollback()
autocommit enabled
-Assuming that when autocommit is enabled that a savepoint is released after the next executable statement.
**/
    public void Var032 ()
    {
        if(checkSavepointSupport())
        {
            try
            {
                //auto-commit enabled
                connection_.setAutoCommit(true);
                stmt=connection_.createStatement();
                Savepoint svpt1=connection_.setSavepoint();
                stmt.executeUpdate("DELETE FROM " + JDSavepointTest.SAVEPOINTTEST_GET + " WHERE C_INTEGER=9");
                connection_.releaseSavepoint(svpt1);
                failed("Didn't throw SQLException");
            }
            catch(Exception e)
            {
                try
                {
                    assertExceptionIsInstanceOf(e, "java.sql.SQLException");
                    stmt.close();
                }
                catch(Exception s)
                {
                    failed (s, "Unexpected Exception");
                }
            }
        }
    }  //if 9 is in the table it should have been deleted.  Otherwise the table should stay the same.

/**
setSavepoint()
rollback()
When two savepoints are set and no work is done between them, a rollback should work.
**/
    public void Var033 ()
    {
        if(checkSavepointSupport())
        {
            try
            {
                //auto-commit disabled
                connection_.setAutoCommit(false);
                stmt=connection_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs=stmt.executeQuery("SELECT * FROM " + JDSavepointTest.SAVEPOINTTEST_GET);
                rs.last();
                int oldrows = rs.getRow();
                connection_.setSavepoint();
                Savepoint svpt2=connection_.setSavepoint();
                connection_.rollback(svpt2);
                rs=stmt.executeQuery("SELECT * FROM " + JDSavepointTest.SAVEPOINTTEST_GET);
                rs.last();
                int newrows = rs.getRow();
                rs.close();
                stmt.close();
                assertCondition( oldrows == newrows);
            }
            catch(Exception e)
            {
                failed(e, "Unexpected Exception");
            }
        }
    } //no changes should have been made to the table.

/**
setSavepoint()
rollback()
-After setting a save point, and then deleting a row from the table, when you rollback,
their should be no changes to the table.
**/
    public void Var034()
    {
        if(checkSavepointSupport())
        {
            try
            {
                //disable auto-commit
                connection_.setAutoCommit(false);
                stmt=connection_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs=stmt.executeQuery("SELECT * FROM " + JDSavepointTest.SAVEPOINTTEST_GET);
                rs.last();
                int oldrows = rs.getRow();
                Savepoint svpt1=connection_.setSavepoint("SAVEPOINT_1");
                stmt.executeUpdate("DELETE FROM " + JDSavepointTest.SAVEPOINTTEST_GET + " WHERE C_INTEGER=1");
                connection_.rollback(svpt1);
                rs=stmt.executeQuery("SELECT * FROM " + JDSavepointTest.SAVEPOINTTEST_GET);
                rs.last();
                int newrows = rs.getRow();
                rs.close();
                stmt.close();
                assertCondition( oldrows == newrows);
                
            }
            catch(Exception e)
            {
                failed(e, "Unexpected Exception");
            }
        }
    } //nothing should have been added or deleted from the table


/**
setSavepoint()
getSavepointId()
- You should be able to get the Id of a savepoint.
**/
    public void Var035 ()
    {
        if(checkSavepointSupport())
        {
            try
            {
                //disable auto-commit
                connection_.setAutoCommit(false);
                Savepoint svpt1=connection_.setSavepoint();
                svpt1.getSavepointId();
                succeeded();
            }
            catch (Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setSavepoint()
getSavepointName()
- exception trying to get the name of an unnamed savepoint
**/
    public void Var036 ()
    {
        if(checkSavepointSupport())
        {
            try
            {
                //disable auto-commit
                connection_.setAutoCommit(false);
                Savepoint svpt1=connection_.setSavepoint();
                svpt1.getSavepointName();                 
                failed("Didn't throw SQLException");
            }
            catch (SQLException e)
            {  
               String message = e.getMessage().toUpperCase();
               if (message.indexOf("SAVEPOINT") >= 0)
                  succeeded();
               else if (message.indexOf("FUNCTION SEQUENCE ERROR") >= 0)
		  succeeded();
	       else
                  failed (e, "Unexpected Exception");
            }
            catch (Exception e)
            {
               failed (e, "Unexpected Exception");
            }
        }
    }

//TEST NOTE:  TESTCASES 28-32 Deal with autocommit and it's relationships to savepoints.  Since
//at the time this testcase was created the relationship was unknown, testcases 28-32 may
//need to be changed.

/**
setSavepoint()
releaseSavepoint()
commit()
-Trying to release a Savepoint from a different connection
**/
    public void Var037 ()
    {
	 { 

	    boolean exceptionThrown = false; 
	    boolean exceptionTested=false; 
	    if(checkSavepointSupport())
	    {
		try
		{
		//disable auto-commit
		    connection_.setAutoCommit(false);
		    stmt=connection_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		    ResultSet rs=stmt.executeQuery("SELECT * FROM " + JDSavepointTest.SAVEPOINTTEST_GET);
		    rs.last();
		    int oldrows = rs.getRow();
		    Savepoint svpt1=connection_.setSavepoint("SAVEPOINT_1");
		    stmt.executeUpdate("INSERT INTO " + JDSavepointTest.SAVEPOINTTEST_GET + " (C_INTEGER) VALUES(21)");
		//
		// Try to release savepoint should throw exception 
		//
		    connection2_.setAutoCommit(false); 
		    try {
			exceptionTested=true;
			connection2_.releaseSavepoint(svpt1);
			exceptionThrown = false; 
		    } catch (SQLException ex) {
			exceptionThrown=true; 
		    } 
		    connection_.releaseSavepoint(svpt1);
		    stmt.executeUpdate("DELETE FROM " + JDSavepointTest.SAVEPOINTTEST_GET + " WHERE C_INTEGER=21");
		    connection_.commit();
		    rs=stmt.executeQuery("SELECT * FROM " + JDSavepointTest.SAVEPOINTTEST_GET);
		    rs.last();
		    int newrows = rs.getRow();
		    rs.close();
		    stmt.close();
		    assertCondition( exceptionThrown && (oldrows == newrows), "exceptionThrown="+exceptionThrown+" sb true oldrows="+oldrows+" newrows="+newrows );
		}
		catch(Exception e)
		{
		    failed(e, "Unexpected Exception exceptionTested="+exceptionTested+" exceptionThrow="+exceptionThrown);
		}
	    }
	}
    } //no changes should have been made to the table


/**
setSavepoint()
rollback()
commit()
-rollback from another connection should throw and exception
**/
    public void Var038 ()
    {
	{ 

	    boolean exceptionTested = false;
	    boolean exceptionThrown = false; 
	    if(checkSavepointSupport())
	    {
		try
		{
		//disable auto-commit
		    connection_.setAutoCommit(false);
		    stmt=connection_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		    ResultSet rs = stmt.executeQuery("SELECT * FROM " + JDSavepointTest.SAVEPOINTTEST_GET);
		    rs.last();
		    int oldrows = rs.getRow();
		    stmt.executeUpdate("INSERT INTO " + JDSavepointTest.SAVEPOINTTEST_GET + " (C_INTEGER) VALUES(2)");
		    Savepoint svpt1=connection_.setSavepoint("SAVEPOINT_1");
		    stmt.executeUpdate("INSERT INTO " + JDSavepointTest.SAVEPOINTTEST_GET + " (C_INTEGER) VALUES(3)");
		    try {
			connection2_.setAutoCommit(false);
			exceptionTested=true; 
			connection2_.rollback(svpt1);
			exceptionThrown=false;
		    } catch (SQLException ex) {
			exceptionThrown=true;
		    }
		//
		// cleanup
		// 
		    connection_.rollback(svpt1);
		    connection_.commit();
		    rs = stmt.executeQuery ("SELECT * FROM " + JDSavepointTest.SAVEPOINTTEST_GET);
		    rs.last();
		    int newrows = rs.getRow();
		    rs.close();
		    stmt.close();
		    assertCondition ( exceptionThrown && ( newrows == (oldrows + 1)),
				      "exceptionThrown="+exceptionThrown);
		}
		catch(Exception e)
		{
		    failed (e, "Unexpected Exception exceptionTested="+exceptionTested+" exceptionThrown="+exceptionThrown);
		}

	    }
	}
    } //table now contains one additional row


/**
setSavepoint()
getSavepointId()
- You shouldn't be able to get the Id of a named savepoint.
**/
    public void Var039 ()
    {
	{ 
	    if(checkSavepointSupport())
	    {
		try
		{
		//disable auto-commit
		    connection_.setAutoCommit(false);
		    Savepoint svpt1=connection_.setSavepoint("VAR035SP");
		    try {
			int id = svpt1.getSavepointId();
			assertCondition(false, "getSavepointId should fail on named savepoint id="+id); 
		    } catch (SQLException ex) {
			succeeded();
		    }
		}
		catch (Exception e)
		{
		    failed (e, "Unexpected Exception");
		}
	    }
	}
    }



} 
