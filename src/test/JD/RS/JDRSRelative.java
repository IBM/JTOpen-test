///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSRelative.java
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
Testcase JDRSRelative.  This tests the following
methods of the JDBC ResultSet class:

<ul>
<li>relative()
</ul>
**/
public class JDRSRelative
extends JDTestcase {



    // Private data.
    private DatabaseMetaData    dmd_;
    private Statement           statement_;
    private Statement           statement2_;



/**
Constructor.
**/
    public JDRSRelative (AS400 systemObject,
                         Hashtable namesAndVars,
                         int runMode,
                         FileOutputStream fileOutputStream,
                         
                         String password)
    {
        super (systemObject, "JDRSRelative",
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
        if (isJdbc20 ()) {
            connection_ = testDriver_.getConnection (baseURL_ + ";data truncation=true", userId_, encryptedPassword_);
            dmd_ = connection_.getMetaData ();
            statement_ = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                                                      ResultSet.CONCUR_UPDATABLE);

            // This statement is used for variations that
            // need to test with the max rows set.
            statement2_ = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                       ResultSet.CONCUR_READ_ONLY);
            statement2_.setMaxRows (50);
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
            statement_.close ();
            statement2_.close ();
            connection_.close ();
        }
    }



/**
relative() - Should return false on an empty result set.
**/
    public void Var001 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS + " WHERE ID = -1");
                boolean success1 = rs.relative (1);
                rs.close ();
                assertCondition (success1 == false);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
relative() - Should throw an exception on a closed result set.
**/
    public void Var002 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.close ();
                rs.relative (1);
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
relative() - Should throw an exception on a cancelled statement.
**/
    public void Var003 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                statement_.cancel ();
                rs.relative (1);
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
relative() - Should throw an exception on a foward only result set.
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
                rs.relative (10);
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
relative() - Should work on a 1 row result set.
**/
    public void Var005 ()
    {

	if (getDriver() == JDTestDriver.DRIVER_NATIVE &&
	    getRelease() <= JDTestDriver.RELEASE_V7R1M0) {
	    notApplicable("Native fails in V5R3, need fix for 9C30869");
	    return; 
	} 

        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS + " WHERE ID = 1");
              
                boolean success1 = rs.relative (1);
                rs.next ();
                boolean success2 = rs.relative (1);
                rs.close ();
                //@pdc first relative should be true, since it contains 1 row
                assertCondition ((success1 == true)  && (success2 == false),
				 "\nFor query first rs.relative(1) returned "+success1+" sb true since 1 row in in query" +
				 "\nlast rs.relative(1) returned "+success2+" sb false"+
                                 "\nUpdated by toolbox 06/05/2008");
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
relative() - Passing 0 on a large result set should leave the
cursor where it is.
**/
    public void Var006 ()
    {

        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.absolute (76);
                boolean success = rs.relative (0);
                int id1 = rs.getInt (1);
                rs.close ();
                assertCondition ((success == true) && (id1 == 76));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
relative() - Passing 1 on a large result set should position
the cursor on the first row and return true.
**/
    public void Var007 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.absolute (73);
                boolean success = rs.relative (1);
                int id1 = rs.getInt (1);
                rs.close ();
                assertCondition ((success == true) && (id1 == 74));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
relative() - Passing a number that will position us on the
last row on a large result set should position the cursor on
the last row and return true.
**/
    public void Var008 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.next ();
                boolean success = rs.relative (98);
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
relative() - Passing a number greater than the row index of
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
                boolean success1 = rs.relative (199);
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
relative() - Passing -1 on a large result set should position
the cursor to the previous row and return true.
**/
    public void Var010 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.next ();
                rs.next ();
                rs.next ();
                rs.next ();
                boolean success = rs.relative (-1);
                int id1 = rs.getInt (1);
                rs.close ();
                assertCondition ((success == true) && (id1 == 3));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
relative() - Passing a negative number that positions the
cursor on the first row on a large result set should position
the cursor on the first row and return true.
**/
    public void Var011 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.absolute (76);
                boolean success = rs.relative (-75);
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
relative() - Passing a number less than the negative row index of
the last row on a large result set should position the cursor
before the last row and return false.
**/
    public void Var012 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.next ();
                rs.next ();
                boolean success1 = rs.relative (-199);
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
relative() - Try to move relative from before the first row.
Should return false and leave the cursor positioned before
the first row.

Fixed in V5R5 by native driver.  The javadoc says  relative(1) is 
indentical to calling next.  So relative(1) should return true. 
 
**/
    public void Var013 ()
    {
       //Made applicable
       /*if (isToolboxDriver())
        {
            notApplicable("todo.  Look into making toolbox do select, and then rs.relative(1) behave same as rs.next()... ");
            return;
        }*/


        String comment=" -- changed by native 01/10/2007 -- relative(1) should be equivalent to next()"; 
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                boolean success1 = rs.relative (1);     /* This must be 1 */ 
                boolean success2 = rs.isBeforeFirst ();
                rs.close ();
                if (getRelease() > JDTestDriver.RELEASE_V7R1M0 ||
                    isToolboxDriver() ) { 
                  assertCondition ((success1 == true) && (success2 == false), 
                        "rs.relative(1) returned "+success1+" sb true "+
                        "rs.isBeforeFirst returned "+success2+" sb false"+
                        comment);
                
                } else { 
                  assertCondition ((success1 == false) && (success2 == true),
                      "rs.relative(1) returned "+success1+" sb false "+
                      "rs.isBeforeFirst returned "+success2+" sb true");
                }       
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
relative() - Try to move relative from after the last row.
Should return false and leave the cursor positioned after
the last row.

Fixed in V5R5 by native driver.  The javadoc says  relative(-1) is 
indentical to calling previous.  So relative(-1) should return true. 

**/
    public void Var014 ()
    {
    	//Made applicable
    	/*
        if (isToolboxDriver())
        {
            notApplicable("todo.  Look into making toolbox do select, and then rs.relative(-1) behave same as rs.previous()... ");
            return;
        }*/
    	
      String comment=" -- changed by native 01/10/2007 -- relative(-1) should be equivalent to previous()"; 
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.afterLast ();
                boolean success1 = rs.relative (-1);
                boolean success2 = rs.isAfterLast ();
                rs.close ();
                if (getRelease() > JDTestDriver.RELEASE_V7R1M0 || isToolboxDriver()) { 
                  assertCondition ((success1 == true) && (success2 == false), 
                        "rs.relative(-1) returned "+success1+" sb true "+
                        "rs.isAfterLast returned "+success2+" sb false"+
                        comment);
                
                } else { 
                  assertCondition ((success1 == false) && (success2 == true),
                      "rs.relative(-1) returned "+success1+" sb false "+
                      "rs.isAfterLastFirst returned "+success2+" sb true");
                }       
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
relative() - Passing 0 on a "simple" result set should not move
the cursor and return true.

Note:  As of JDBC 4.0, the metadata functions are defined to be
FORWARD only.  Consequently, rs.relative() will not function. 
Changed to not applicable since rs.relative (1) is tested by
Var 007 

**/
    public void Var015 ()
    {
	if (getRelease() >= JDTestDriver.RELEASE_V7R1M0 || isJdbc40()) {
	    notApplicable("In V5R5 or JDBC40, metafunctions are forward only"); 
	} else { 
	    if (checkJdbc20 ()) {
		try {
		    ResultSet rs = dmd_.getTableTypes ();
		    rs.next ();
		    boolean success = rs.relative (1);
		    String s1 = rs.getString ("TABLE_TYPE");
		    rs.close ();
		    assertCondition ((success == true) && (s1 != null));
		}
		catch (Exception e) {
		    failed (e, "Unexpected Exception");
		}
	    }
	}
    }



/**
relative() - Passing 1 on a "simple" result set should position
the cursor on the next row and return true.
**/
    public void Var016 ()
    {
	if (getRelease() >= JDTestDriver.RELEASE_V7R1M0 || isJdbc40()) {
	    notApplicable("In V5R5, metafunctions are forward only"); 
	} else { 
	    if (checkJdbc20 ()) {
		try {
		    ResultSet rs = dmd_.getTableTypes ();
		    rs.next ();
		    boolean success = rs.relative (1);
		    String s1 = rs.getString ("TABLE_TYPE");
		    rs.close ();
		    assertCondition ((success == true) && (s1 != null));
		}
		catch (Exception e) {
		    failed (e, "Unexpected Exception");
		}
	    }
	}
    }



/**
relative() - Passing a number that positions the cursor to the
last row on a "simple" result set should position the cursor on
the last row and return true.
**/
    public void Var017 ()
    {
	if (getRelease() >= JDTestDriver.RELEASE_V7R1M0 || isJdbc40()) {
	    notApplicable("In JDBC 4.0, metafunctions are forward only"); 
	} else { 
	    if (checkJdbc20 ()) {
		try {
		    ResultSet rs = dmd_.getTableTypes ();
		    rs.next ();
		    boolean success = rs.relative (2);
		    String s1 = rs.getString ("TABLE_TYPE");
		    rs.close ();
		    assertCondition ((success == true) && (s1 != null));
		}
		catch (Exception e) {
		    failed (e, "Unexpected Exception");
		}
	    }
	}
    }



/**
relative() - Passing a number greater than the row index of
the last row on a "simple" result set should position the cursor
after the last row and return false.
**/
    public void Var018 ()
    {
        if (checkJdbc20 ()) {
            try {
		ResultSet rs;
		if (getRelease() >= JDTestDriver.RELEASE_V7R1M0 || isJdbc40()) {
		    rs = statement_.executeQuery ("SELECT * FROM "
						  + JDRSTest.RSTEST_POS);
		} else { 
		    rs = dmd_.getTableTypes ();
		}
                rs.next ();
                rs.next ();
                boolean success1 = rs.relative (199);
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
relative() - Passing -1 on a "simple" result set should position
the cursor on the previous row and return true.
**/
    public void Var019 ()
    {
        if (checkJdbc20 ()) {
            try {
		ResultSet rs; 
		if (getRelease() >= JDTestDriver.RELEASE_V7R1M0 || isJdbc40()) {
		    rs = statement2_.executeQuery ("SELECT DISTINCT TABLE_TYPE FROM SYSIBM.SQLTABLES");
		} else { 
		    rs = dmd_.getTableTypes ();
		}
                rs.last ();
                boolean success = rs.relative (-1);
                String s1 = rs.getString ("TABLE_TYPE");
                rs.close ();
                assertCondition ((success == true) && (s1 != null));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
relative() - Passing a negative number that positions the cursor
on the first row of a "simple" result set should position the cursor
on the first row and return true.
**/
    public void Var020 ()
    {
        if (checkJdbc20 ()) {
            try {
		ResultSet rs; 
		if (getRelease() >= JDTestDriver.RELEASE_V7R1M0 || isJdbc40() ) {
		    rs = statement2_.executeQuery ("SELECT DISTINCT TABLE_TYPE FROM SYSIBM.SQLTABLES");
		} else { 

		    rs = dmd_.getTableTypes ();
		}
                rs.last ();
                boolean success = rs.relative (-2);
                String s1 = rs.getString ("TABLE_TYPE");
                rs.close ();
                assertCondition ((success == true) && (s1 != null));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
relative() - Passing a number less than the negative row index of
the last row on a "simple" result set should position the cursor
before the first row and return false.
**/
    public void Var021 ()
    {
        if (checkJdbc20 ()) {
            try {
		ResultSet rs; 
		if ( getRelease() >= JDTestDriver.RELEASE_V7R1M0 || isJdbc40() ) {
		    rs = statement2_.executeQuery ("SELECT DISTINCT TABLE_TYPE FROM SYSIBM.SQLTABLES");
		} else { 

		    rs = dmd_.getTableTypes ();
		}
                rs.next ();
                rs.next ();
                boolean success1 = rs.relative (-199);
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
relative() - Passing 0 on a "mapped" result set should not
move the cursor and return true.

SQL400 - We can't support scrollable metadata resultsets (CLI/DB restriction).
**/
    public void Var022 ()
    {
        if (isToolboxDriver() && !isSysibmMetadata()) {
            if (checkJdbc20 ()) {
                try {
                    ResultSet rs = dmd_.getColumns (null, "QIWS",
                                                    "QCUSTCDT", "%");
                    rs.next ();
                    boolean success = rs.relative (0);
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
relative() - Passing 1 on a "mapped" result set should position
the cursor to the next row and return true.

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
                    boolean success = rs.relative (1);
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
relative() - Passing the row index of the last row on a "mapped"
result set should position the cursor on the last row and return
true.

SQL400 - We can't support scrollable metadata resultsets (CLI/DB restriction).
**/
    public void Var024 ()
    {
        if (isToolboxDriver() && !isSysibmMetadata()) {
            if (checkJdbc20 ()) {
                try {
                    ResultSet rs = dmd_.getColumns (null, "QIWS",
                                                    "QCUSTCDT", "%");
                    rs.next ();
                    boolean success = rs.relative (10);
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
relative() - Passing a number greater than the row index of
the last row on a "mapped" result set position the cursor to
after the last row and return false.

SQL400 - We can't support scrollable metadata resultsets (CLI/DB restriction).
**/
    public void Var025 ()
    {
        if (isToolboxDriver() && !isSysibmMetadata()) {
            if (checkJdbc20 ()) {
                try {
                    ResultSet rs = dmd_.getColumns (null, "QIWS",
                                                    "QCUSTCDT", "%");
                    rs.next ();
                    rs.next ();
                    boolean success1 = rs.relative (199);
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
relative() - Passing -1 on a "mapped" result set should position
the cursor on the previous row and return true.

SQL400 - We can't support scrollable metadata resultsets (CLI/DB restriction).
**/
    public void Var026 ()
    {
        if (isToolboxDriver() && !isSysibmMetadata()) {
            if (checkJdbc20 ()) {
                try {
                    ResultSet rs = dmd_.getColumns (null, "QIWS",
                                                    "QCUSTCDT", "%");
                    rs.last ();
                    boolean success = rs.relative (-1);
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
relative() - Passing the negative row index of the last row on a
"mapped" result set should position the cursor on the last row and
return true.

SQL400 - We can't support scrollable metadata resultsets (CLI/DB restriction).
**/
    public void Var027 ()
    {
        if (isToolboxDriver() && !isSysibmMetadata()) {
            if (checkJdbc20 ()) {
                try {
                    ResultSet rs = dmd_.getColumns (null, "QIWS",
                                                    "QCUSTCDT", "%");
                    rs.next ();
                    rs.next ();
                    rs.next ();
                    boolean success = rs.relative (-2);
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
relative() - Passing a number less than the negative row index of
the last row on a "mapped" result set should position the cursor
before the first row and return false.

SQL400 - We can't support scrollable metadata resultsets (CLI/DB restriction).
**/
    public void Var028 ()
    {
        if (isToolboxDriver() && !isSysibmMetadata()) {
            if (checkJdbc20 ()) {
                try {
                    ResultSet rs = dmd_.getColumns (null, "QIWS",
                                                    "QCUSTCDT", "%");
                    rs.next ();
                    rs.next ();
                    boolean success1 = rs.relative (-199);
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
relative() - Should return false after a beforeFirst().

Nope.. should work after a beforeLast if there are at least
23 rows. 
Fixed in V5R5 
**/
    public void Var029 ()
    {
        //Made applicable
    	/*
        if (isToolboxDriver())
        {
            notApplicable("todo.  Look into making toolbox making rs.relative() work same as native in 550... ");
            return;
        }*/


      String comment=" -- changed by native 01/10/2007 -- relative() should work on beforeLast"; 

        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.beforeFirst ();
                boolean success = rs.relative (23);
                rs.close ();
                if (getRelease() > JDTestDriver.RELEASE_V7R1M0 || isToolboxDriver()) {
                  assertCondition (success == true, comment);
                 
                } else { 
                  assertCondition (success == false);
                }       
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
relative() - Should return true after a first().
**/
    public void Var030 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.first ();
                boolean success = rs.relative (24);
                int id = rs.getInt (1);
                rs.close ();
                assertCondition ((success == true) && (id == 25));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
relative() - Should return true after an absolute().
**/
    public void Var031 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT ID, VALUE FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.absolute (50);
                boolean success = rs.relative (25);
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
relative() - Should return true after a relative().
**/
    public void Var032 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.first ();
                rs.relative (75);
                boolean success = rs.relative (-44);
                int id = rs.getInt ("ID");
                rs.close ();
                assertCondition ((success == true) && (id == 32));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
relative() - Should return true after a next().
**/
    public void Var033 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.next ();
                boolean success = rs.relative (5);
                int id = rs.getInt (1);
                rs.close ();
                assertCondition ((success == true) && (id == 6));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
relative() - Should return true after a previous().
**/
    public void Var034 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.last ();
                rs.previous ();
                boolean success = rs.relative (-5);
                int id = rs.getInt (1);
                rs.close ();
                assertCondition ((success == true) && (id == 93));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
relative() - Should return true after a last().
**/
    public void Var035 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.last ();
                boolean success = rs.relative (-10);
                int id = rs.getInt (1);
                rs.close ();
                assertCondition ((success == true) && (id == 89));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
relative() - Should return false after an afterLast().

Nope:  should return true. 
**/
    public void Var036 ()
    {
    	//Made applicable
    	/*
        if (isToolboxDriver())
        {
            notApplicable("todo.  Look into making toolbox making rs.relative() work same as native in 550... ");
            return;
        }*/
    	
      String comment=" -- changed by native 01/10/2007 -- relative() should work after afterLast"; 

        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.afterLast ();
                boolean success = rs.relative (-50);
                rs.close ();
                if (getRelease() > JDTestDriver.RELEASE_V7R1M0 || isToolboxDriver()) { 
                  assertCondition (success == true, comment);
                } else { 
                   assertCondition (success == false);
                }
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
relative() - Should return true after a moveToInsertRow().
**/
    public void Var037 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT ID, VALUE FROM "
                                                        + JDRSTest.RSTEST_POS + " FOR UPDATE");
                rs.absolute (-43);
                rs.moveToInsertRow ();
                boolean success = rs.relative (5);
                int id1 = rs.getInt ("ID");
                rs.close ();
                assertCondition ((success == true) && (id1 == 62));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
relative() - Should return true after a moveToInsertRow(),
then moveToCurrentRow().
**/
    public void Var038 ()
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
                boolean success = rs.relative (-34);
                int id1 = rs.getInt ("ID");
                rs.close ();
                assertCondition ((success == true) && (id1 == 62));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
relative() - Should clear any warnings.
**/
    public void Var039 ()
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
                rs.relative (50);
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
relative() - Set max rows and fetch the rows using only relative(+).
Max rows should be honored.
**/
    public void Var040 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement2_.executeQuery ("SELECT * FROM "
                                                         + JDRSTest.RSTEST_POS);
                rs.next ();
                boolean success1 = rs.relative (49);
                boolean success2 = rs.relative (2);
                rs.close ();
                assertCondition ((success1 == true) && (success2 == false));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
relative() - Set max rows and fetch the rows using
relative(-) .  This is the case where we lose
track of the row number.  Max rows should not be honored.
**/
    public void Var041 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement2_.executeQuery ("SELECT * FROM "
                                                         + JDRSTest.RSTEST_POS);
                rs.last ();
                boolean success1 = rs.relative (-5);
                boolean success2 = rs.relative (-5);
                rs.close ();
                rs.close ();
                assertCondition ((success1 == true) && (success2 == true));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
relative() - Update the rows using relative().
**/
    public void Var042 ()
    {
        if (checkJdbc20 ()) {
            try {
                // Update each value.
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS + " FOR UPDATE OF VALUE");
                PreparedStatement ps = connection_.prepareStatement ("UPDATE "
                                                                     + JDRSTest.RSTEST_POS + " SET VALUE = 'RELATIVE' WHERE CURRENT OF "
                                                                     + rs.getCursorName ());
                rs.next ();
                ps.execute ();
                for (int i = 98, s = 1; i != 0; --i, s *= -1) {
                    rs.relative (i * s);
                    ps.execute ();
                }

                // Go through the result set again as a check.
                // It is okay to just use next() here.
                ResultSet rs2 = statement_.executeQuery ("SELECT * FROM "
                                                         + JDRSTest.RSTEST_POS);
                boolean success = true;
                int count = 0;
                while (rs2.next ()) {
                    ++count;
                    if (! rs2.getString (2).equals ("RELATIVE"))
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



// TEST NOTE: It would be nice to verify that relative() implicity
//            closes a previously retrieved InputStream.  However
//            it is not obvious how to check that an InputStream
//            has been closed.


}



