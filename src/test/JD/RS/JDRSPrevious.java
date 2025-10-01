///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSPrevious.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test.JD.RS;

import java.io.FileOutputStream;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDRSTest;
import test.JDTestcase;



/**
Testcase JDRSPrevious.  This tests the following
methods of the JDBC ResultSet class:

<ul>
<li>previous()
</ul>
**/
public class JDRSPrevious
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSPrevious";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDRSTest.main(newArgs); 
   }



    // Private data.
    private DatabaseMetaData    dmd_;
    private Statement           statement_;
    private Statement           statement2_;



/**
Constructor.
**/
    public JDRSPrevious (AS400 systemObject,
                         Hashtable<String,Vector<String>> namesAndVars,
                         int runMode,
                         FileOutputStream fileOutputStream,
                         
                         String password)
    {
        super (systemObject, "JDRSPrevious",
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
previous() - Should return false on an empty result set.
**/
    public void Var001 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS + " WHERE ID = -1");
                rs.afterLast ();
                boolean success = rs.previous ();
                rs.close ();
                assertCondition (success == false);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
previous() - Should throw an exception on a closed result set.
**/
    public void Var002 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.afterLast ();
                rs.close ();
                rs.previous ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
previous() - Should throw an exception on a cancelled statement.
**/
    public void Var003 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.afterLast ();
                statement_.cancel ();
                rs.previous ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
previous() - Should throw an exception on a foward only result set.
**/
    public void Var004 ()
    {
        if (checkJdbc20 ()) {
            try {
                Statement s = connection_.createStatement (ResultSet.TYPE_FORWARD_ONLY,
                                                           ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = s.executeQuery ("SELECT * FROM "
                                               + JDRSTest.RSTEST_POS);
                rs.afterLast ();
                rs.previous ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
previous() - Should work on a 1 row result set.
**/
    public void Var005 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS + " WHERE ID = 1");
                rs.afterLast ();
                boolean success1 = rs.previous ();
                int id1 = rs.getInt (1);
                boolean success2 = rs.previous ();
                rs.close ();
                assertCondition ((success1 == true) && (success2 == false)
                                 && (id1 == 1));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
previous() - Should work on a large result set.
**/
    public void Var006 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.afterLast ();
                boolean success = true;
                int count = 0;
                while (rs.previous ()) {
                    if (rs.getInt (1) != 99 - count)
                        success = false;
                    ++count;
                }
                rs.close ();
                assertCondition ((success == true) && (count == 99));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
previous() - Should work on a "simple" result set.
**/
    public void Var007 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs ;

		rs = statement2_.executeQuery("select * from SYSIBM.SYSTBLTYPE");
                rs.afterLast ();
                boolean success = true;
                int count = 0;
                while (rs.previous ()) {
                    ++count;
                    if (rs.getString ("TABLE_TYPE") == null)
                        success = false;
                }
                rs.close ();

	    /* Changed the comparision -- @D3C */ 
	    int expectedCount = 3;
	    if (true) {
		// add 1 for MQT
		expectedCount++; 
	    }
	    if (true) {
		// add 1 for ALIAS
		expectedCount++; 
	    }

	    assertCondition ((success == true) && (count == expectedCount), "Changed 08/03/05 by native driver, success="+success+" count = "+count+" sb "+expectedCount );

/*
                if(getRelease() < JDTestDriver.RELEASE_V5R3M0 ||	    //@D1A
		   getDriver() == JDTestDriver.DRIVER_NATIVE)               // @D2
                    assertCondition ((success == true) && (count == 3));
                else                                                        //@D1A
                    assertCondition ((success == true) && (count == 4));    //@D1A
*/
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
previous() - Should work on a "mapped" result set.

SQL400 - We can't support scrollable metadata resultsets (CLI/DB restriction). B1C
**/
    public void Var008 ()
    {
        if (isToolboxDriver() && !isSysibmMetadata()) {
            if (checkJdbc20 ()) {
                try {
                    ResultSet rs = dmd_.getColumns (null, "QIWS",
                                                    "QCUSTCDT", "%");
                    rs.afterLast ();
                    boolean success = true;
                    int count = 0;
                    while (rs.previous ()) {
                        ++count;
                        if (! rs.getString ("TABLE_NAME").startsWith ("QCUSTCDT"))
                            success = false;
                    }
                    rs.close ();
                    assertCondition ((success == true) && (count == 11));
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
previous() - Should return false after a beforeFirst().
**/
    public void Var009 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.beforeFirst ();
                boolean success = rs.previous ();
                rs.close ();
                assertCondition (success == false);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
previous() - Should return false after a first().
**/
    public void Var010 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.first ();
                boolean success = rs.previous ();
                rs.close ();
                assertCondition (success == false);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
previous() - Should return true after an absolute().
**/
    public void Var011 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.absolute (50);
                boolean success = rs.previous ();
                int id = rs.getInt (1);
                rs.close ();
                assertCondition ((success == true) && (id == 49));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
previous() - Should return true after a relative().
**/
    public void Var012 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.next ();
                rs.relative (75);
                boolean success = rs.previous ();
                int id = rs.getInt (1);
                rs.close ();
                assertCondition ((success == true) && (id == 75));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
previous() - Should return true after a next().
**/
    public void Var013 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.absolute (75);
                rs.next ();
                boolean success = rs.previous ();
                int id = rs.getInt (1);
                rs.close ();
                assertCondition ((success == true) && (id == 75));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
previous() - Should return true after a last().
**/
    public void Var014 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.last ();
                boolean success = rs.previous ();
                int id = rs.getInt (1);
                rs.close ();
                assertCondition ((success == true) && (id == 98));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
previous() - Should return true after an afterLast().
**/
    public void Var015 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.afterLast ();
                boolean success = rs.previous ();
                int id = rs.getInt (1);
                rs.close ();
                assertCondition ((success == true) && (id == 99));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
previous() - Should return true after a moveToInsertRow().
**/
    public void Var016 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS + " FOR UPDATE");
                rs.absolute (-43);
                rs.moveToInsertRow ();
                boolean success = rs.previous ();
                int id1 = rs.getInt ("ID");
                rs.close ();
                assertCondition ((success == true) && (id1 == 56));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
previous() - Should return true after a moveToInsertRow(),
then moveToCurrentRow().
**/
    public void Var017 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS + " FOR UPDATE");
                rs.last ();
                rs.previous ();
                rs.previous ();
                rs.previous ();
                rs.moveToInsertRow ();
                rs.moveToCurrentRow ();
                boolean success = rs.previous ();
                int id1 = rs.getInt ("ID");
                rs.close ();
                assertCondition ((success == true) && (id1 == 95));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
previous() - Should clear any warnings.
**/
    @SuppressWarnings("deprecation")
    public void Var018 ()
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
                rs.previous ();
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
previous() - Set max rows and fetch the rows using only previous().
Max rows should not be honored.

Max rows should be honored and the native JDBC driver will do so. 
**/
    public void Var019 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement2_.executeQuery ("SELECT * FROM "
                                                         + JDRSTest.RSTEST_POS);
                rs.afterLast ();
                int count = 0;
                while (rs.previous ())
                    ++count;
                rs.close ();
                assertCondition (count == 50);

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
previous() - Set max rows and fetch the rows using
absolute(+) and previous().  This is the case where we can
always keep track of the row number.  Max rows should still
not be honored (at least there is no way to tell).

Max rows should be honored but this test doesn't do anything that
has anything to do with it.  You move 45 rows into a 50 row ResultSet 
and then position backwards.  This should work 44 times before
failing.  If absolute(55) would have worked, the user should only 
be able to scroll backwards 50 times as that is the total size of 
the ResultSet.
**/
    public void Var020 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement2_.executeQuery ("SELECT * FROM "
                                                         + JDRSTest.RSTEST_POS);
                boolean success = true;
                int count = 0;
                rs.absolute (45);
                while (rs.previous ())
                    ++count;
                rs.close ();
                assertCondition ((success == true) && (count == 44));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
previous() - Set max rows and fetch the rows using
absolute(-) and previous().  This is the case where we lose
track of the row number.  Max rows should not be honored.

Max rows should be honored.  The absolute(-55) positioned the 
cursor before the first row, therefore no calls to previous work.
**/
    public void Var021 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement2_.executeQuery ("SELECT * FROM "
                                                         + JDRSTest.RSTEST_POS);
                rs.afterLast ();
                boolean success = true;
                int count = 0;
                rs.absolute (-55);
                while (rs.previous ())
                    ++count;
                rs.close ();
                assertCondition ((success == true) && (count == 0));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
previous() - Update the rows using previous().
**/
    public void Var022 ()
    {
        if (checkJdbc20 ()) {
            try {
                // Update each value.
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS + " FOR UPDATE OF VALUE");
                PreparedStatement ps = connection_.prepareStatement ("UPDATE "
                                                                     + JDRSTest.RSTEST_POS + " SET VALUE = 'PREVIOUS' WHERE CURRENT OF "
                                                                     + rs.getCursorName ());
                rs.afterLast ();
                while (rs.previous ())
                    ps.execute ();
                rs.close ();
                ps.close ();

                // Go through the result set again as a check.
                // It is okay to just use next() here.
                ResultSet rs2 = statement_.executeQuery ("SELECT * FROM "
                                                         + JDRSTest.RSTEST_POS);
                boolean success = true;
                int count = 0;
                while (rs2.next ()) {
                    ++count;
                    if (! rs2.getString (2).equals ("PREVIOUS"))
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


/**
previous() - In Jitterbug #2402, the second previous was incorrectly returning true.
**/
    public void Var023 ()
    {
	if (checkJdbc20 ()) {
	    try
	    {
		ResultSet rs = 
		  statement_.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_POS);

		rs.absolute(4); 
		rs.beforeFirst(); 
		boolean firstNext = rs.next(); 
		boolean firstPrevious = rs.previous(); 
		boolean secondPrevious = rs.previous();   
		rs.close();

		assertCondition(firstNext && !firstPrevious && !secondPrevious);
	    }
	    catch (Exception e)
	    {
		failed(e, "Unexpected exception.");
	    }
	}
    }



// TEST NOTE: It would be nice to verify that previous() implicity
//            closes a previously retrieved InputStream.  However
//            it is not obvious how to check that an InputStream
//            has been closed.


}



