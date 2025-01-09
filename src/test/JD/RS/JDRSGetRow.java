///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSGetRow.java
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
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable; import java.util.Vector;



/**
Testcase JDRSGetRow.  This tests the following
methods of the JDBC ResultSet classes:

<ul>
<li>getRow()
</ul>
**/
public class JDRSGetRow
extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSGetRow";
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
    public JDRSGetRow (AS400 systemObject,
                       Hashtable<String,Vector<String>> namesAndVars,
                       int runMode,
                       FileOutputStream fileOutputStream,
                       
                       String password)
    {
        super (systemObject, "JDRSGetRow",
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
            connection_ = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
            dmd_ = connection_.getMetaData ();
	    try {
		statement_ = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
							  ResultSet.CONCUR_UPDATABLE);
	    } catch (SQLException sqlex) {
		statement_ = connection_.createStatement ();
	    }
            // This statement is used for variations that
            // need to test with the max rows set.
	    try {
            statement2_ = connection_.createStatement (	ResultSet.TYPE_SCROLL_INSENSITIVE,
						       ResultSet.CONCUR_READ_ONLY);
	    } catch (SQLException sqlex) {
		statement2_ = connection_.createStatement ();
	    }
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
getRow() - Should return 0 on an empty result set.
**/
    public void Var001 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS + " WHERE ID = -1");
                int row = rs.getRow ();
                rs.close ();
                assertCondition (row == 0);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getRow() - Should throw an exception on a closed result set.
**/
    public void Var002 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.close ();
                rs.getRow ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getRow() - Should throw an exception on a cancelled statement.
**/
    public void Var003 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                statement_.cancel ();
                rs.getRow ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getRow() - Should work on a 1 row result set.
**/
    public void Var004 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS + " WHERE ID = 1");
                rs.first ();
                int row = rs.getRow ();
                rs.close ();
                assertCondition (row == 1);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getRow() - Should work on a large result set.
**/
    public void Var005 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.absolute (65);
                int row = rs.getRow ();
                rs.close ();
                assertCondition (row == 65);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getRow() - Should work on a "simple" result set.
**/
    public void Var006 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = dmd_.getTableTypes ();
                rs.next ();
                rs.next ();
                int row = rs.getRow ();
                rs.close ();
                assertCondition (row == 2);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getRow() - Should work on a "mapped" result set.

SQL400 - We can't support scrollable metadata resultsets (CLI/DB restriction).
**/
    public void Var007 ()
    {
        if (isToolboxDriver() && !isSysibmMetadata()) {
            if (checkJdbc20 ()) {
                try {
                    ResultSet rs = dmd_.getColumns (null, "QIWS",
                                                    "QCUSTCDT", "%");
                    rs.next ();
                    rs.relative (10);
                    int row = rs.getRow ();
                    rs.close ();
                    assertCondition (row == 11);
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
getRow() - Should return 0 after a beforeFirst().
**/
    public void Var008 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.beforeFirst ();
                int row = rs.getRow ();
                rs.close ();
                assertCondition (row == 0);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
getRow() - Should return 1 after a first().
**/
    public void Var009 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.first ();
                int row = rs.getRow ();
                rs.close ();
                assertCondition (row == 1);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
getRow() - Should return the row number after an absolute(+).
**/
    public void Var010 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.absolute (50);
                int row = rs.getRow ();
                rs.close ();
                assertCondition (row == 50);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
getRow() - Should return 0 after an absolute(-).  The Native JDBC driver will not return
0 here anymore.  The correct row number (55) will be returned.  It is expected that soon
the Toolbox JDBC driver will also do this.
**/
    public void Var011 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.absolute (-45);
                int row = rs.getRow ();
                rs.close ();

                assertCondition (row == 55);
            }

            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
getRow() - Should return the row number after a relative(+) with
the row index valid.
**/
    public void Var012 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.next ();
                rs.relative (75);
                int row = rs.getRow ();
                rs.close ();
                assertCondition (row == 76);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
getRow() - Should return the row number after a relative(-) with
the row index valid.
**/
    public void Var013 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.absolute (35);
                rs.relative (-5);
                int row = rs.getRow ();
                rs.close ();
                assertCondition (row == 30);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
getRow() - Should return 0 after a relative(+) with
the row index not valid.  This should work through the native
JDBC driver now... the absolute(-54) should position the cursor
on row 46 and then the relative(15) should position the cursor
on row 61.
**/
    public void Var014 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.absolute (-54);
                rs.relative (15);

                int row = rs.getRow ();
                rs.close ();

                assertCondition (row == 61);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
getRow() - Should return 0 after a relative(-) with
the row index not valid.  Native JDBC can now handle this.  We position
to row 99 with the call to last.  Then we back up 5 to 94.
**/
    public void Var015 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);

                rs.last ();
                rs.relative (-5);

                int row = rs.getRow ();
                rs.close ();

                assertCondition (row == 94);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
getRow() - Should return the row number after a next() when the
row index is valid.
**/
    public void Var016 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.next ();
                rs.next ();
                rs.next ();
                int row = rs.getRow ();
                rs.close ();
                assertCondition (row == 3);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getRow() - Should return 0 after a next() when the
row index is not valid.  This will work through Native
JDBC.  The absolute(-10) places the cursor on row 90 and
the next() repositions the cursor to row 91.
**/
    public void Var017 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.absolute (-10);
                rs.next ();

                int row = rs.getRow ();
                rs.close ();

                assertCondition (row == 91);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getRow() - Should return the row number after a previous() when
the index is valid.
**/
    public void Var018 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT ID, VALUE FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.absolute (10);
                rs.previous ();
                int row = rs.getRow ();
                rs.close ();
                assertCondition (row == 9);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getRow() - Should return 0 after a previous() when
the index is not valid.  Native JDBC will correctly handle
this. The call to afterLast places the cursor at the
end of the ResultSet.  Doing previous from there, will place
the cursor on the last row of the ResultSet (99).
**/
    public void Var019 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT ID, VALUE FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.afterLast ();
                rs.previous ();

                int row = rs.getRow ();
                rs.close ();

                assertCondition (row == 99);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getRow() - Should return 0 after a last().  Native JDBC will correctly handle this
sequence.  Calling last will place the cursor on the last row of the ResultSet (99)
**/
    public void Var020 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.last ();
                int row = rs.getRow ();
                rs.close ();

                assertCondition (row == 99);

            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
getRow() - Should return 0 after an afterLast().
**/
    public void Var021 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.afterLast ();
                int row = rs.getRow ();
                rs.close ();
                assertCondition (row == 0);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getRow() - Should return 0 after a moveToInsertRow().
**/
    public void Var022 ()
    {
        if (checkJdbc20 ()) {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            notApplicable("JCC does not support moveToInserRow");
            return;
          }
            try {
                ResultSet rs = statement_.executeQuery ("SELECT ID, VALUE FROM "
                                                        + JDRSTest.RSTEST_POS + " FOR UPDATE");
                rs.absolute (43);
                rs.moveToInsertRow ();

                int row = rs.getRow ();

                rs.close ();
                assertCondition (row == 0);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getRow() - Should return the row number after a moveToInsertRow(),
then moveToCurrentRow(), when the row index is valid.
**/
    public void Var023 ()
    {
        if (checkJdbc20 ()) {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            notApplicable("JCC does not support moveToInserRow");
            return;
          }
            try {
                ResultSet rs = statement_.executeQuery ("SELECT ID, VALUE FROM "
                                                        + JDRSTest.RSTEST_POS + " FOR UPDATE");
                rs.absolute (14);
                rs.next ();
                rs.moveToInsertRow ();
                rs.moveToCurrentRow ();
                int row = rs.getRow ();
                rs.close ();
                assertCondition (row == 15);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getRow() - Should return 0 after a moveToInsertRow(),
then moveToCurrentRow(), when the row index is not valid.  Native
JDBC can handle this sequence and will return the row number (96).
**/
    public void Var024 ()
    {
        if (checkJdbc20 ()) {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            notApplicable("JCC does not support moveToInserRow");
            return;
          }
            try {
                ResultSet rs = statement_.executeQuery ("SELECT ID, VALUE FROM "
                                                        + JDRSTest.RSTEST_POS + " FOR UPDATE");
                rs.last ();
                rs.previous ();
                rs.previous ();
                rs.previous ();
                rs.moveToInsertRow ();
                rs.moveToCurrentRow ();

                int row = rs.getRow ();
                rs.close ();

                assertCondition (row == 96);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



}



