///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSAbsolute.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test.JD.RS;

import com.ibm.as400.access.AS400;

import test.JDRSTest;
import test.JDTestDriver;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.Hashtable;



/**
Testcase JDRSAbsolute.  This tests the following
methods of the JDBC ResultSet class:

<ul>
<li>absolute()
</ul>
**/
public class JDRSAbsolute
extends JDTestcase {



    // Private data.
    private DatabaseMetaData    dmd_;
    private Statement           statement_;
    private Statement           statement2_;



/**
Constructor.
**/
    public JDRSAbsolute (AS400 systemObject,
                         Hashtable namesAndVars,
                         int runMode,
                         FileOutputStream fileOutputStream,
                         
                         String password)
    {
        super (systemObject, "JDRSAbsolute",
               namesAndVars, runMode, fileOutputStream,
               password);
    }



/**
Performs setup needed before running variations.

@exception Exception If an exception occurs.
**/
    protected void setup ()
    throws Exception
    {	if (connection_ != null) connection_.close();

        if (isJdbc20 ()) {
            connection_ = testDriver_.getConnection (baseURL_ + ";data truncation=true", userId_, encryptedPassword_, "JDRSAbsolute");
            dmd_ = connection_.getMetaData ();
	    try { 
		statement_ = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
							  ResultSet.CONCUR_UPDATABLE);

	    // This statement is used for variations that
	    // need to test with the max rows set.
		statement2_ = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
							   ResultSet.CONCUR_READ_ONLY);
		statement2_.setMaxRows (50);
	    } catch (Exception e) {
		System.out.println("Warning: Exception during setup");
		e.printStackTrace(); 
	    } 
        }
    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
    throws Exception
    {
        if (isJdbc20 ()) {
	    if (statement_ != null) statement_.close ();
	    if (statement2_ != null) statement2_.close ();
	    if (connection_ != null) connection_.close ();
        }
    }



/**
absolute() - Should return false on an empty result set.
**/
    public void Var001 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS + " WHERE ID = -1");
                boolean success1 = rs.absolute (1);
                rs.close ();
                assertCondition (success1 == false);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
absolute() - Should throw an exception on a closed result set.
**/
    public void Var002 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.close ();
                rs.absolute (1);
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
absolute() - Should throw an exception on a cancelled statement.
**/
    public void Var003 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                statement_.cancel ();
                rs.absolute (1);
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
absolute() - Should throw an exception on a foward only result set.
**/
    public void Var004 ()
    {
        if (checkJdbc20 ()) {
            try {
                Statement s = connection_.createStatement (ResultSet.TYPE_FORWARD_ONLY,
                                                           ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = s.executeQuery ("SELECT * FROM "
                                               + JDRSTest.RSTEST_POS);
                rs.next ();
                rs.absolute (10);
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
absolute() - Should work on a 1 row result set.
**/
    public void Var005 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS + " WHERE ID = 1");
                boolean success1 = rs.absolute (1);
                int id1 = rs.getInt (1);
                boolean success2 = rs.absolute (2);
                rs.close ();
                assertCondition ((success1 == true)
                        && (success2 == false) && (id1 == 1));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
absolute() - Passing 0 on a large result set should throw
an exception.

This is defined differently in the JDBC 3.0 specification. Page 119
says that "Calling absolute(0) moves the cursor before the first row."
**/
    public void Var006 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                /* boolean success = */  rs.absolute (0);

                assertCondition(rs.isBeforeFirst());
            }
            catch (Exception e) 
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
absolute() - Passing 1 on a large result set should position
the cursor on the first row and return true.
**/
    public void Var007 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                boolean success = rs.absolute (1);
                int id1 = rs.getInt (1);
                rs.close ();
                assertCondition ((success == true) && (id1 == 1));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
absolute() - Passing the row index of the last row on a large
result set should position the cursor on the last row and return
true.
**/
    public void Var008 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                boolean success = rs.absolute (99);
                int id1 = rs.getInt (1);
                rs.close ();
                assertCondition ((success == true) && (id1 == 99));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
absolute() - Passing a number greater than the row index of
the last row on a large result set should position the cursor
after the last row and return false.
**/
    public void Var009 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.next ();
                rs.next ();
                boolean success1 = rs.absolute (199);
                boolean success2 = rs.isAfterLast ();
                rs.close ();
                assertCondition ((success1 == false) && (success2 == true));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
absolute() - Passing -1 on a large result set should position
the cursor on the last row and return true.
**/
    public void Var010 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                boolean success = rs.absolute (-1);
                int id1 = rs.getInt (1);
                rs.close ();
                assertCondition ((success == true) && (id1 == 99));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
absolute() - Passing the negative row index of the last row on a
large result set should position the cursor on the last row and
return true.
**/
    public void Var011 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                boolean success = rs.absolute (-99);
                int id1 = rs.getInt (1);
                rs.close ();
                assertCondition ((success == true) && (id1 == 1));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
absolute() - Passing a number less than the negative row index of
the last row on a large result position the cursor before the
first row and return false.
**/
    public void Var012 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.next ();
                rs.next ();
                boolean success1 = rs.absolute (-199);
                boolean success2 = rs.isBeforeFirst ();
                rs.close ();
                assertCondition ((success1 == false) && (success2 == true));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
absolute() - Passing 0 on a "simple" result set should throw
an exception.

This is defined differently in the JDBC 3.0 specification. Page 119
says that "Calling absolute(0) moves the cursor before the first row."
**/
    public void Var013 ()
    {
        if (checkJdbc20 ()) {
            try {
		ResultSet rs;
		/* In V5R5 the metadata results are no longer scrollable */
		/* run a different query */
		if (getRelease() >= JDTestDriver.RELEASE_V7R1M0 || isJdbc40()) {
		    // Note:  don't use statement_ since it is updatable 
		    rs = statement2_.executeQuery("select * from SYSIBM.SYSDUMMY1"); 
		} else { 
		    rs = dmd_.getTableTypes ();
		}
                /* boolean success = */  rs.absolute (0);
                assertCondition(rs.isBeforeFirst(), "isBeforeFirst() returned false, but should have returned true");
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
absolute() - Passing 1 on a "simple" result set should position
the cursor on the first row and return true.
**/
    public void Var014 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs ;
		boolean success ;
		String s1;

		if (getRelease() >= JDTestDriver.RELEASE_V7R1M0 || isJdbc40()) {
		    rs = statement2_.executeQuery("select * from SYSIBM.SYSDUMMY1"); 
		    success = rs.absolute (1);
		    s1 = rs.getString ("IBMREQD");

		} else { 
		    rs = dmd_.getTableTypes ();
		    success = rs.absolute (1);
		    s1 = rs.getString ("TABLE_TYPE");
		}

                rs.close ();
                assertCondition ((success == true) && (s1 != null));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
absolute() - Passing the row index of the last row on a "simple"
result set should position the cursor on the last row and return
true.
**/
    public void Var015 ()
    {
        if (checkJdbc20 ()) {
            try {

                ResultSet rs ;
		boolean success ;
		String s1;

		if (getRelease() >= JDTestDriver.RELEASE_V7R1M0 || isJdbc40()) {
		    rs = statement2_.executeQuery("select * from SYSIBM.SYSTBLTYPE"); 
		    success = rs.absolute (3);
		    s1 = rs.getString ("TABLE_TYPE");

		} else { 
		    rs = dmd_.getTableTypes ();
		    success = rs.absolute (3);
		    s1 = rs.getString ("TABLE_TYPE");
		}
                if (s1 == null) {
                  System.out.println("TABLE_TYPE returned null"); 
                  s1=""; 
                }
                rs.close ();
                assertCondition ((success == true) && (s1.length () > 0));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
absolute() - Passing a number greater than the row index of
the last row on a "simple" result set should position the cursor
after the last row and return false.
**/
    public void Var016 ()
    {
        if (checkJdbc20 ()) {
            try {

                ResultSet rs ;

		if (getRelease() >= JDTestDriver.RELEASE_V7R1M0 || isJdbc40()) {
		    rs = statement2_.executeQuery("select * from SYSIBM.SYSTBLTYPE"); 
		} else { 
		    rs = dmd_.getTableTypes ();
		}
                rs.next ();
                rs.next ();
                boolean success1 = rs.absolute (199);
                boolean success2 = rs.isAfterLast ();
                rs.close ();
                assertCondition ((success1 == false) && (success2 == true));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
absolute() - Passing -1 on a "simple" result set should position
the cursor on the last row and return true.
**/
    public void Var017 ()
    {
        if (checkJdbc20 ()) {
            try {

                ResultSet rs ;
		boolean success ;
		String s1;

		if (getRelease() >= JDTestDriver.RELEASE_V7R1M0 || isJdbc40()) {
		    rs = statement2_.executeQuery("select * from SYSIBM.SYSTBLTYPE"); 
		    success = rs.absolute (-1);
		    s1 = rs.getString ("TABLE_TYPE");
		} else { 
		    rs = dmd_.getTableTypes ();
		    success = rs.absolute (-1);
		    s1 = rs.getString ("TABLE_TYPE");
		}
                if (s1 == null) {
                  System.out.println("TABLE_TYPE returned null"); 
                  s1=""; 
                }
                rs.close ();
                assertCondition ((success == true) && (s1.length () > 0));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
absolute() - Passing the negative row index of the last row on a
"simple" result set should position the cursor on the last row and
return true.
**/
    public void Var018 ()
    {
        if (checkJdbc20 ()) {
            try {
		ResultSet rs;
		if (getRelease() >= JDTestDriver.RELEASE_V7R1M0 || isJdbc40()) {
		    rs = statement2_.executeQuery("select * from SYSIBM.SYSTBLTYPE"); 
		} else {
		    rs = dmd_.getTableTypes ();
		}
                boolean success = rs.absolute (-3);
                String s1 = rs.getString ("TABLE_TYPE");
                rs.close ();
                assertCondition ((success == true) && (s1 != null), "success = "+success+" sb true  s1 = "+s1+" sb != null");
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
absolute() - Passing a number less than the negative row index of
the last row on a "simple" result set should position the cursor
before the first row and return false.
**/
    public void Var019 ()
    {
        if (checkJdbc20 ()) {
            try {
		ResultSet rs;
		if (getRelease() >= JDTestDriver.RELEASE_V7R1M0 || isJdbc40()) {
		    rs = statement2_.executeQuery("select * from SYSIBM.SYSTBLTYPE"); 
		} else {

		    rs = dmd_.getTableTypes ();
		}
                rs.next ();
                rs.next ();
                boolean success1 = rs.absolute (-199);
                boolean success2 = rs.isBeforeFirst ();
                rs.close ();
                assertCondition ((success1 == false) && (success2 == true));
            }
            catch (Exception e) 
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
absolute() - Passing 0 on a "mapped" result set should throw
an exception.

SQL400 - We can't support scrollable metadata resultsets (CLI/DB restriction).
**/
    public void Var020 ()
    {
        if (isToolboxDriver() && !isSysibmMetadata()) 
        {
            if (checkJdbc20 ()) 
            {
                try 
                {
                    ResultSet rs = dmd_.getColumns (null, "QIWS",
                                                    "QCUSTCDT", "%");
                    /* boolean success =  */ rs.absolute (0);
                    assertCondition(rs.isBeforeFirst());
                }
                catch (Exception e) 
                {
                   failed (e, "Unexpected Exception");
                }
            }
        }
        else {
            notApplicable();
        }
    }



/**
absolute() - Passing 1 on a "mapped" result set should position
the cursor on the first row and return true.

SQL400 - We can't support scrollable metadata resultsets (CLI/DB restriction).
**/
    public void Var021 ()
    {
        if (isToolboxDriver() && !isSysibmMetadata()) {
            if (checkJdbc20 ()) {
                try {
                    ResultSet rs = dmd_.getColumns (null, "QIWS",
                                                    "QCUSTCDT", "%");
                    boolean success = rs.absolute (1);
                    String s1 = rs.getString ("TABLE_NAME");
                    rs.close ();
                    assertCondition ((success == true) && (s1.startsWith ("QCUSTCDT")));
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
            }
        }
        else {
            notApplicable();
        }
    }



/**
absolute() - Passing the row index of the last row on a "mapped"
result set should position the cursor on the last row and return
true.

SQL400 - We can't support scrollable metadata resultsets (CLI/DB restriction).
**/
    public void Var022 ()
    {
        if (isToolboxDriver() && !isSysibmMetadata()) {
            if (checkJdbc20 ()) {
                try {
                    ResultSet rs = dmd_.getColumns (null, "QIWS",
                                                    "QCUSTCDT", "%");
                    boolean success = rs.absolute (11);
                    String s1 = rs.getString ("TABLE_NAME");
                    rs.close ();
                    assertCondition ((success == true) && (s1.startsWith ("QCUSTCDT")));
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
            }
        }
        else {
            notApplicable();
        }
    }



/**
absolute() - Passing a number greater than the row index of
the last row on a "mapped" result set should position the
cursor after the last row and return false.

SQL400 - We can't support scrollable metadata resultsets (CLI/DB restriction).
**/
    public void Var023 ()
    {
        if (isToolboxDriver() && !isSysibmMetadata()) {
            if (checkJdbc20 ()) {
                try {
                    ResultSet rs = dmd_.getColumns (null, "QIWS",
                                                    "QCUSTCDT", "%");
                    rs.next ();
                    rs.next ();
                    boolean success1 = rs.absolute (199);
                    boolean success2 = rs.isAfterLast ();
                    rs.close ();
                    assertCondition ((success1 == false) && (success2 == true));
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
            }
        }
        else {
            notApplicable();
        }
    }



/**
absolute() - Passing -1 on a "mapped" result set should position
the cursor on the last row and return true.

SQL400 - We can't support scrollable metadata resultsets (CLI/DB restriction).
**/
    public void Var024 ()
    {
        if (isToolboxDriver() && !isSysibmMetadata()) {
            if (checkJdbc20 ()) {
                try {
                    ResultSet rs = dmd_.getColumns (null, "QIWS",
                                                    "QCUSTCDT", "%");
                    boolean success = rs.absolute (-1);
                    String s1 = rs.getString ("TABLE_NAME");
                    rs.close ();
                    assertCondition ((success == true) && (s1.startsWith ("QCUSTCDT")));
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
            }
        }
        else {
            notApplicable();
        }
    }



/**
absolute() - Passing the negative row index of the last row on a
"mapped" result set should position the cursor on the last row and
return true.

SQL400 - We can't support scrollable metadata resultsets (CLI/DB restriction).
**/
    public void Var025 ()
    {
        if (isToolboxDriver() && !isSysibmMetadata()) {
            if (checkJdbc20 ()) {
                try {
                    ResultSet rs = dmd_.getColumns (null, "QIWS",
                                                    "QCUSTCDT", "%");
                    boolean success = rs.absolute (-11);
                    String s1 = rs.getString ("TABLE_NAME");
                    rs.close ();
                    assertCondition ((success == true) && (s1.startsWith ("QCUSTCDT")));
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
            }
        }
        else {
            notApplicable();
        }
    }



/**
absolute() - Passing a number less than the negative row index of
the last row on a "mapped" result set should position the cursor
befroe the first row and return false.

SQL400 - We can't support scrollable metadata resultsets (CLI/DB restriction).
**/
    public void Var026 ()
    {
        if (isToolboxDriver() && !isSysibmMetadata() ) {
            if (checkJdbc20 ()) {
                try {
                    ResultSet rs = dmd_.getColumns (null, "QIWS",
                                                    "QCUSTCDT", "%");
                    rs.next ();
                    rs.next ();
                    boolean success1 = rs.absolute (-199);
                    boolean success2 = rs.isBeforeFirst ();
                    rs.close ();
                    assertCondition ((success1 == false) && (success2 == true));
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception");
                }
            }
        }
        else {
            notApplicable();
        }
    }


/**
absolute() - Should return true after a beforeFirst().
**/
    public void Var027 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.beforeFirst ();
                boolean success = rs.absolute (23);
                int id = rs.getInt (1);
                rs.close ();
                assertCondition ((success == true) && (id == 23));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
absolute() - Should return true after a first().
**/
    public void Var028 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.first ();
                boolean success = rs.absolute (-23);
                int id = rs.getInt (1);
                rs.close ();
                assertCondition ((success == true) && (id == 77));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
absolute() - Should return true after an absolute().
**/
    public void Var029 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT ID, VALUE FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.absolute (50);
                boolean success = rs.absolute (75);
                int id = rs.getInt ("ID");
                rs.close ();
                assertCondition ((success == true) && (id == 75));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
absolute() - Should return true after a relative().
**/
    public void Var030 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.first ();
                rs.relative (75);
                boolean success = rs.absolute (-44);
                int id = rs.getInt ("ID");
                rs.close ();
                assertCondition ((success == true) && (id == 56));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
absolute() - Should return true after a next().
**/
    public void Var031 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.next ();
                boolean success = rs.absolute (5);
                int id = rs.getInt (1);
                rs.close ();
                assertCondition ((success == true) && (id == 5));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
absolute() - Should return true after a previous().
**/
    public void Var032 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.last ();
                rs.previous ();
                boolean success = rs.absolute (-5);
                int id = rs.getInt (1);
                rs.close ();
                assertCondition ((success == true) && (id == 95));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
absolute() - Should return true after a last().
**/
    public void Var033 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.last ();
                boolean success = rs.absolute (10);
                int id = rs.getInt (1);
                rs.close ();
                assertCondition ((success == true) && (id == 10));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
absolute() - Should return true after an afterLast().
**/
    public void Var034 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.afterLast ();
                boolean success = rs.absolute (-50);
                int id = rs.getInt (1);
                rs.close ();
                assertCondition ((success == true) && (id == 50));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
absolute() - Should return true after a moveToInsertRow().
**/
    public void Var035 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT ID, VALUE FROM "
                                                        + JDRSTest.RSTEST_POS + " FOR UPDATE");
                rs.absolute (-43);
                rs.moveToInsertRow ();
                boolean success = rs.absolute (34);
                int id1 = rs.getInt ("ID");
                rs.close ();
                assertCondition ((success == true) && (id1 == 34));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
absolute() - Should return true after a moveToInsertRow(),
then moveToCurrentRow().
**/
    public void Var036 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT ID, VALUE FROM "
                                                        + JDRSTest.RSTEST_POS + " FOR UPDATE");
                rs.last ();
                rs.previous ();
                rs.previous ();
                rs.previous ();
                rs.moveToInsertRow ();
                rs.moveToCurrentRow ();
                boolean success = rs.absolute (-34);
                int id1 = rs.getInt ("ID");
                rs.close ();
                assertCondition ((success == true) && (id1 == 66));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
absolute() - Should clear any warnings.
**/
    public void Var037 ()
    {
        if (checkJdbc20 ()) {
            try {
                Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                                                           ResultSet.CONCUR_UPDATABLE);
                ResultSet rs = s.executeQuery ("SELECT C_KEY,C_CHAR_50 FROM " + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "CHAR_FLOAT");

                // Force a warning (data truncation).
                rs.getBigDecimal("C_CHAR_50", 0);

                SQLWarning before = rs.getWarnings ();
                rs.absolute (3);
                SQLWarning after = rs.getWarnings ();
                rs.close ();
                s.close ();
                assertCondition ((before != null) && (after == null));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
absolute() - Set max rows and fetch the rows using only absolute(+).
Max rows should be honored.
**/
    public void Var038 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement2_.executeQuery ("SELECT * FROM "
                                                         + JDRSTest.RSTEST_POS);
                boolean success1 = rs.absolute (50);
                boolean success2 = rs.absolute (51);
                rs.close ();
                assertCondition ((success1 == true) && (success2 == false));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
absolute() - Set max rows and fetch the rows using
absolute(-) .  This is the case where we lose
track of the row number.  Max rows should not be honored.
**/
    public void Var039 ()
    {
        if (checkJdbc20 ()) 
        {
            try 
            {
                ResultSet rs = statement2_.executeQuery ("SELECT * FROM "
                                                         + JDRSTest.RSTEST_POS);
                boolean success1 = rs.absolute (-50);
                boolean success2 = rs.absolute (-51);
                rs.close ();
                rs.close ();
                assertCondition ((success1 == true) && (success2 == false));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
absolute() - Update the rows using absolute().
**/
    public void Var040 ()
    {
        if (checkJdbc20 ()) {
            try {
                // Update each value.
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS + " FOR UPDATE OF VALUE");
                PreparedStatement ps = connection_.prepareStatement ("UPDATE "
                                                                     + JDRSTest.RSTEST_POS + " SET VALUE = 'ABSOLUTE' WHERE CURRENT OF "
                                                                     + rs.getCursorName ());
                for (int i = 99; i >= 1; --i) {
                    rs.absolute (i);
                    ps.execute ();
                }
                rs.close ();
                ps.close ();

                // Go through the result set again as a check.
                // It is okay to just use absolute() here.
                ResultSet rs2 = statement_.executeQuery ("SELECT * FROM "
                                                         + JDRSTest.RSTEST_POS);
                boolean success = true;
                int count = 0;
                while (rs2.next ()) {
                    ++count;
                    if (! rs2.getString (2).equals ("ABSOLUTE"))
                        success = false;
                }
                rs2.close ();

                assertCondition ((success == true) && (count == 99));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



// TEST NOTE: It would be nice to verify that absolute() implicity
//            closes a previously retrieved InputStream.  However
//            it is not obvious how to check that an InputStream
//            has been closed.


}



