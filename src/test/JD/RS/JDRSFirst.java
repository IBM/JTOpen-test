///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSFirst.java
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
import test.JDTestcase;
import test.JD.JDSerializeFile;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;



/**
Testcase JDRSFirst.  This tests the following
methods of the JDBC ResultSet classes:

<ul>
<li>first()
<li>isFirst()
</ul>
**/
public class JDRSFirst
extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSFirst";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDRSTest.main(newArgs); 
   }



    // Private data.
    private DatabaseMetaData    dmd_;
    private Statement           statement_;
    private Statement           statement2_;


    private Connection          connectionNoPrefetch_;
    private DatabaseMetaData    dmdNoPrefetch_;
    private Statement           statementNoPrefetch_;
    private Statement           statement2NoPrefetch_;

/**
Constructor.
**/
    public JDRSFirst (AS400 systemObject,
                      Hashtable<String,Vector<String>> namesAndVars,
                      int runMode,
                      FileOutputStream fileOutputStream,
                      
                      String password)
    {
        super (systemObject, "JDRSFirst",
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
            connection_ = testDriver_.getConnection (baseURL_ + ";data truncation=true", userId_, encryptedPassword_,"JDRSFirst1");
            dmd_ = connection_.getMetaData ();
            statement_ = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                                                      ResultSet.CONCUR_UPDATABLE);

            // This statement is used for variations that
            // need to test with the max rows set.
            statement2_ = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                       ResultSet.CONCUR_READ_ONLY);
            statement2_.setMaxRows (50);

	    connectionNoPrefetch_ = testDriver_.getConnection (baseURL_ + ";data truncation=true;prefetch=false", userId_, encryptedPassword_,"JDRSFirst2");
            dmdNoPrefetch_ = connectionNoPrefetch_.getMetaData ();
            statementNoPrefetch_ = connectionNoPrefetch_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                                                      ResultSet.CONCUR_UPDATABLE);

            statement2NoPrefetch_ = connectionNoPrefetch_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                       ResultSet.CONCUR_READ_ONLY);
            statement2NoPrefetch_.setMaxRows (50);


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
            connection_.commit();
            statement_.close ();
            statement2_.close ();
            connection_.close ();

	    statementNoPrefetch_.close();
	    connectionNoPrefetch_.close(); 

        }
    }

    public void cleanupConnections ()
    throws Exception
    {
        if (isJdbc20 ()) {
            connection_.commit();
            connection_.close ();

      connectionNoPrefetch_.close(); 

        }
    }



/**
first() - Should return false on an empty result set.
**/
    public void Var001 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS + " WHERE ID = -1");
                boolean success = rs.first ();
                rs.close ();
                assertCondition (success == false);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
first() - Should throw an exception on a closed result set.
**/
    public void Var002 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.close ();
                rs.first ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
first() - Should throw an exception on a cancelled statement.
**/
    public void Var003 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                statement_.cancel ();
                rs.first ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
first() - Should throw an exception on a foward only result set.
**/
    public void Var004 ()
    {
        if (checkJdbc20 ()) {
            try {
                Statement s = connection_.createStatement (ResultSet.TYPE_FORWARD_ONLY,
                                                           ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = s.executeQuery ("SELECT * FROM "
                                               + JDRSTest.RSTEST_POS);
                rs.first ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
first() - Should work on a 1 row result set.
**/
    public void Var005 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS + " WHERE ID = 1");
                boolean success = rs.first ();
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
first() - Should work on a large result set.
**/
    public void Var006 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                boolean success = rs.first ();
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
first() - Should work on a "simple" result set.
**/
    public void Var007 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs ;

		rs = statement2_.executeQuery("select * from SYSIBM.SYSTBLTYPE");
                boolean success = rs.first ();
                String s1 = rs.getString ("TABLE_TYPE");
                rs.close ();
                assertCondition ((success == true) && (s1 != null), "success="+success+" tableType="+s1+" sb not null");
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
first() - Should work on a "mapped" result set.

SQL400 - We can't support scrollable metadata resultsets (CLI/DB restriction).
**/
    public void Var008 ()
    {
        if (isToolboxDriver() && !isSysibmMetadata()) {
            if (checkJdbc20 ()) {
                try {
                    ResultSet rs = dmd_.getColumns (null, "QIWS",
                                                    "QCUSTCDT", "%");
                    boolean success = rs.first ();
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
first() - Should return true after a beforeFirst().
**/
    public void Var009 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.beforeFirst ();
                boolean success = rs.first ();
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
first() - Should return true after a first().
**/
    public void Var010 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.first ();
                boolean success = rs.first ();
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
first() - Should return true after an absolute().
**/
    public void Var011 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.absolute (50);
                boolean success = rs.first ();
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
first() - Should return true after a relative().
**/
    public void Var012 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.relative (75);
                boolean success = rs.first ();
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
first() - Should return true after a next().
**/
    public void Var013 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.next ();
                boolean success = rs.first ();
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
first() - Should return true after a previous().
**/
    public void Var014 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT ID, VALUE FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.last ();
                rs.previous ();
                boolean success = rs.first ();
                int id1 = rs.getInt ("ID");
                rs.close ();
                assertCondition ((success == true) && (id1 == 1));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
first() - Should return true after a last().
**/
    public void Var015 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.last ();
                boolean success = rs.first ();
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
first() - Should return true after an afterLast().
**/
    public void Var016 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.afterLast ();
                boolean success = rs.first ();
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
first() - Should return true after a moveToInsertRow().
**/
    public void Var017 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT ID, VALUE FROM "
                                                        + JDRSTest.RSTEST_POS + " FOR UPDATE");
                rs.absolute (-43);
                rs.moveToInsertRow ();
                boolean success = rs.first ();
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
first() - Should return true after a moveToInsertRow(),
then moveToCurrentRow().
**/
    public void Var018 ()
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
                boolean success = rs.first ();
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
first() - Should clear any warnings.
**/
    @SuppressWarnings("deprecation")
    public void Var019 ()
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
                rs.first ();
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
first() - Set max rows and fetch the rows using first().
Max rows should not affect this.
**/
    public void Var020 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement2_.executeQuery ("SELECT * FROM "
                                                         + JDRSTest.RSTEST_POS);
                boolean success = rs.first ();
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
first() - Update the rows using first().
**/
    public void Var021 ()
    {
        if (checkJdbc20 ()) {
          JDSerializeFile serializeFile = null;
          try {
            serializeFile = new JDSerializeFile(connection_, JDRSTest.RSTEST_POS);
                // Update each value.
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS + " FOR UPDATE OF VALUE");
                PreparedStatement ps = connection_.prepareStatement ("UPDATE "
                                                                     + JDRSTest.RSTEST_POS + " SET VALUE = 'FIRST' WHERE CURRENT OF "
                                                                     + rs.getCursorName ());
                rs.first ();
                ps.execute ();
                rs.close ();
                ps.close ();

                // Go through the result set again as a check.
                // It is okay to just use next() here.
                ResultSet rs2 = statement_.executeQuery ("SELECT * FROM "
                                                         + JDRSTest.RSTEST_POS);
                rs2.next ();
                String s1 = rs2.getString (2);
                rs2.close ();

                assertCondition (s1.equals ("FIRST"));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            } finally {
              if (serializeFile != null) {
                try {
                  serializeFile.close();
                } catch (SQLException e) {
                  e.printStackTrace();
                }
              }
            }
        }
    }



// TEST NOTE: It would be nice to verify that first() implicity
//            closes a previously retrieved InputStream.  However
//            it is not obvious how to check that an InputStream
//            has been closed.



/**
isFirst() - Should return false on an empty result set with
no positioning.
**/
    public void Var022 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS + " WHERE ID = -1");
                boolean success = rs.isFirst ();
                rs.close ();
                assertCondition (success == false);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isFirst() - Should return false on an empty result set even after
explicitly positioning the cursor using first().
**/
    public void Var023 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS + " WHERE ID = -1");
                rs.first ();
                boolean success = rs.isFirst ();
                rs.close ();
                assertCondition (success == false);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isFirst() - Should throw an exception on a closed result set.
**/
    public void Var024 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.close ();
                rs.isFirst ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
isFirst() - Should throw an exception on a cancelled statement.
**/
    public void Var025 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                statement_.cancel ();
                rs.isFirst ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
isFirst() - Should return false on a 1 row result set when positioned
before the only row.
**/
    public void Var026 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS + " WHERE ID = 1");
                boolean success = rs.isFirst ();
                rs.close ();
                assertCondition (success == false);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isFirst() - Should return true on a 1 row result set when positioned
on the only row.
**/
    public void Var027 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS + " WHERE ID = 1");
                rs.next ();
                boolean success = rs.isFirst ();
                rs.close ();
                assertCondition (success == true);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isFirst() - Should return false on a 1 row result set when positioned
after the only row.
**/
    public void Var028()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS + " WHERE ID = 1");
                rs.next ();
                rs.next ();
                boolean success = rs.isFirst ();
                rs.close ();
                assertCondition (success == false);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isFirst() - Should return false on a large result set when
not on the first row.
**/
    public void Var029 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                boolean success = rs.isFirst ();
                rs.close ();
                assertCondition (success == false);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isFirst() - Should return true on a large result set when
positioned to the first row.
**/
    public void Var030 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.first ();
                boolean success = rs.isFirst ();
                rs.close ();
                assertCondition (success == true);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isFirst() - Should return false on a "simple" result set
when not on the first row.
**/
    public void Var031 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = dmd_.getTableTypes ();
                boolean success = rs.isFirst ();
                rs.close ();
                assertCondition (success == false);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isFirst() - Should return true on a "simple" result set
when positioned to the first row.
**/
    public void Var032 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = dmd_.getTableTypes ();
                rs.next ();
                boolean success = rs.isFirst ();
                rs.close ();
                assertCondition (success == true);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isFirst() - Should work on a "mapped" result set when not on
the first row.

SQL400 - We can't support scrollable metadata resultsets (CLI/DB restriction).
**/
    public void Var033 ()
    {
        if (isToolboxDriver() && !isSysibmMetadata()) {
            if (checkJdbc20 ()) {
                try {
                    ResultSet rs = dmd_.getColumns (null, "QIWS",
                                                    "QCUSTCDT", "%");
                    boolean success = rs.isFirst ();
                    rs.close ();
                    assertCondition (success == false);
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
isFirst() - Should work on a "mapped" result set when positioned
to the first row.

SQL400 - We can't support scrollable metadata resultsets (CLI/DB restriction).
**/
    public void Var034 ()
    {
        if (isToolboxDriver() && !isSysibmMetadata()) {
            if (checkJdbc20 ()) {
                try {
                    ResultSet rs = dmd_.getColumns (null, "QIWS",
                                                    "QCUSTCDT", "%");
                    rs.first ();
                    boolean success = rs.isFirst ();
                    rs.close ();
                    assertCondition (success == true);
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
isFirst() - Should return false after a beforeFirst().
**/
    public void Var035 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.beforeFirst ();
                boolean success = rs.isFirst ();
                rs.close ();
                assertCondition (success == false);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
isFirst() - Should return true after a first().
**/
    public void Var036 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.first ();
                boolean success = rs.isFirst ();
                rs.close ();
                assertCondition (success == true);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
isFirst() - Should return false after an absolute(+) to a
row other than the first.
**/
    public void Var037 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.absolute (50);
                boolean success = rs.isFirst ();
                rs.close ();
                assertCondition (success == false);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
isFirst() - Should return true after an absolute(+) to the
first row.
**/
    public void Var038 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.absolute (1);
                boolean success = rs.isFirst ();
                rs.close ();
                assertCondition (success == true);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
isFirst() - Should return false after an absolute(-) to a
row other than the first.
**/
    public void Var039 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.absolute (-50);
                boolean success = rs.isFirst ();
                rs.close ();
                assertCondition (success == false);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
isFirst() - Should return false after an absolute(-) to the
first row, since the first row can not be checked.
**/
    public void Var040 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.absolute (-99);
                boolean success = rs.isFirst ();
                rs.close ();
                if (isToolboxDriver())
                    assertCondition (success == false);
                else
                    assertCondition (success == true);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isFirst() - Should return false after a relative(+) to a
row other than the first.
**/
    public void Var041 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.next ();
                rs.relative (39);
                boolean success = rs.isFirst ();
                rs.close ();
                assertCondition (success == false);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
isFirst() - Should return true after a relative(+) to the
first row.
**/
    public void Var042 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.next ();
                rs.relative (0);
                boolean success = rs.isFirst ();
                rs.close ();
                assertCondition (success == true);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
isFirst() - Should return false after a relative(-) to a
row other than the first.
**/
    public void Var043 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.last ();
                rs.relative (-50);
                boolean success = rs.isFirst ();
                rs.close ();
                assertCondition (success == false);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
isFirst() - Should return true after a relative(-) to the
first row.
**/
    public void Var044 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.absolute (10);
                rs.relative (-9);
                boolean success = rs.isFirst ();
                rs.close ();
                assertCondition (success == true);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
isFirst() - Should return false after a next() when not on
the first row.
**/
    public void Var045 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.next ();
                rs.next ();
                boolean success = rs.isFirst ();
                rs.close ();
                assertCondition (success == false);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isFirst() - Should return true after a next() when positioned on
the first row.
**/
    public void Var046 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.next ();
                boolean success = rs.isFirst ();
                rs.close ();
                assertCondition (success == true);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isFirst() - Should return false after a previous() when not on
the first row.
**/
    public void Var047 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.absolute (50);
                rs.previous ();
                boolean success = rs.isFirst ();
                rs.close ();
                assertCondition (success == false);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isFirst() - Should return true after a previous() when positioned on
the first row.
**/
    public void Var048 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.absolute (3);
                rs.previous ();
                rs.previous ();
                boolean success = rs.isFirst ();
                rs.close ();
                assertCondition (success == true);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isFirst() - Should return false after a last().
**/
    public void Var049 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.last ();
                boolean success = rs.isFirst ();
                rs.close ();
                assertCondition (success == false);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
isFirst() - Should return false after an afterLast().
**/
    public void Var050 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.afterLast ();
                boolean success = rs.isFirst ();
                rs.close ();
                assertCondition (success == false);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isFirst() - Should return false after a moveToInsertRow().
**/
    public void Var051 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT ID, VALUE FROM "
                                                        + JDRSTest.RSTEST_POS + " FOR UPDATE");
                rs.absolute (-43);
                rs.moveToInsertRow ();
                boolean success = rs.isFirst ();
                rs.close ();
                assertCondition (success == false);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isFirst() - Should return false after a moveToInsertRow(),
then moveToCurrentRow() when not on the first row.
**/
    public void Var052 ()
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
                boolean success = rs.isFirst ();
                rs.close ();
                assertCondition (success == false);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isFirst() - Should return true after a moveToInsertRow(),
then moveToCurrentRow() when not on the first row.
**/
    public void Var053 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement_.executeQuery ("SELECT ID, VALUE FROM "
                                                        + JDRSTest.RSTEST_POS + " FOR UPDATE");
                rs.first ();
                rs.moveToInsertRow ();
                rs.moveToCurrentRow ();
                boolean success = rs.isFirst ();
                rs.close ();
                assertCondition (success == true);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
isFirst() -- Should return false where there is still more data.
                 Also verify that the result set is correct when isBeforeFirst is used when
                 prefetch is off
**/

    public void Var054 ()
    {
	if (checkJdbc20 ()) {
	    if (checkNative()) {
		StringBuffer sb = new StringBuffer(); 
                String sql = "Select 'A' from sysibm.sysdummy1 UNION Select 'B' from sysibm.sysdummy1 UNION SELECT 'C' from sysibm.sysdummy1"; 
		try {
		    boolean passed=true; 
		    ResultSet rs = statementNoPrefetch_.executeQuery (sql);

		    boolean fetch1 = rs.next();
		    if (!fetch1) { passed=false; sb.append("fetch1 returned "+fetch1+"\n"); }

		    boolean First1 = rs.isFirst();
		    if (!First1) { passed=false; sb.append("First1 returned "+First1+"\n"); }

		    String  string1 = rs.getString(1);
		    if (! "A".equals(string1)) { passed = false; sb.append("Expected 'A' got '"+string1+"'\n");}
 

		    boolean fetch2 = rs.next();
		    if (!fetch2) { passed=false; sb.append("fetch2 returned "+fetch2+"\n"); }

		    boolean First2 = rs.isFirst();
		    if (First2) { passed=false; sb.append("First2 returned "+First2+"\n"); }

		    String  string2 = rs.getString(1);
		    if (! "B".equals(string2)) { passed = false; sb.append("Expected 'B' got '"+string2+"'\n");}
 

		    boolean fetch3 = rs.next();
		    if (!fetch3) { passed=false; sb.append("fetch3 returned "+fetch3+"\n"); }

		    boolean First3 = rs.isFirst();
		    if (First3) { passed=false; sb.append("First3 returned "+First3+"\n"); }

		    String  string3 = rs.getString(1);
		    if (! "C".equals(string3)) { passed = false; sb.append("Expected 'C' got '"+string3+"'\n");}


		    boolean fetch4 = rs.next();
		    if (fetch4) { passed=false; sb.append("fetch4 returned "+fetch4+"\n"); }

		    boolean First4 = rs.isFirst();
		    if (First4) { passed=false; sb.append("First4 returned "+First4+"\n"); }

		    assertCondition (passed, sb.toString() +" -- Added by native driver 11/29/2010");
		} catch (Exception e) {
		    failed (e, "Unexpected Exception sql="+sql+" --  added by native driver (11/29/2010)");
		}
	    }
	}
    }


    public void Var055 () { notApplicable(); }
    public void Var056 () { notApplicable(); }
    public void Var057 () { notApplicable(); }
    public void Var058 () { notApplicable(); }
    public void Var059 () { notApplicable(); }
    public void Var060 () { notApplicable(); }

// New Prefetch Testcases


/**
first() - Should return false on an empty result set.
**/
    public void Var061 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS + " WHERE ID = -1");
                boolean success = rs.first ();
                rs.close ();
                assertCondition (success == false);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
first() - Should throw an exception on a closed result set.
**/
    public void Var062 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.close ();
                rs.first ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
first() - Should throw an exception on a cancelled statement.
**/
    public void Var063 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                statementNoPrefetch_.cancel ();
                rs.first ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
first() - Should throw an exception on a foward only result set.
**/
    public void Var064 ()
    {
        if (checkJdbc20 ()) {
            try {
                Statement s = connectionNoPrefetch_.createStatement (ResultSet.TYPE_FORWARD_ONLY,
                                                           ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = s.executeQuery ("SELECT * FROM "
                                               + JDRSTest.RSTEST_POS);
                rs.first ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
first() - Should work on a 1 row result set.
**/
    public void Var065 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS + " WHERE ID = 1");
                boolean success = rs.first ();
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
first() - Should work on a large result set.
**/
    public void Var066 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                boolean success = rs.first ();
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
first() - Should work on a "simple" result set.
**/
    public void Var067 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs ;

		rs = statement2NoPrefetch_.executeQuery("select * from SYSIBM.SYSTBLTYPE");
                boolean success = rs.first ();
                String s1 = rs.getString ("TABLE_TYPE");
                rs.close ();
                assertCondition ((success == true) && (s1 != null), "success="+success+" tableType="+s1+" sb not null");
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
first() - Should work on a "mapped" result set.

SQL400 - We can't support scrollable metadata resultsets (CLI/DB restriction).
**/
    public void Var068 ()
    {
        if (isToolboxDriver() && !isSysibmMetadata()) {
            if (checkJdbc20 ()) {
                try {
                    ResultSet rs = dmdNoPrefetch_.getColumns (null, "QIWS",
                                                    "QCUSTCDT", "%");
                    boolean success = rs.first ();
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
first() - Should return true after a beforeFirst().
**/
    public void Var069 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.beforeFirst ();
                boolean success = rs.first ();
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
first() - Should return true after a first().
**/
    public void Var070 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.first ();
                boolean success = rs.first ();
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
first() - Should return true after an absolute().
**/
    public void Var071 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.absolute (50);
                boolean success = rs.first ();
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
first() - Should return true after a relative().
**/
    public void Var072 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.relative (75);
                boolean success = rs.first ();
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
first() - Should return true after a next().
**/
    public void Var073 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.next ();
                boolean success = rs.first ();
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
first() - Should return true after a previous().
**/
    public void Var074 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT ID, VALUE FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.last ();
                rs.previous ();
                boolean success = rs.first ();
                int id1 = rs.getInt ("ID");
                rs.close ();
                assertCondition ((success == true) && (id1 == 1));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
first() - Should return true after a last().
**/
    public void Var075 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.last ();
                boolean success = rs.first ();
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
first() - Should return true after an afterLast().
**/
    public void Var076 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.afterLast ();
                boolean success = rs.first ();
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
first() - Should return true after a moveToInsertRow().
**/
    public void Var077 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT ID, VALUE FROM "
                                                        + JDRSTest.RSTEST_POS + " FOR UPDATE");
                rs.absolute (-43);
                rs.moveToInsertRow ();
                boolean success = rs.first ();
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
first() - Should return true after a moveToInsertRow(),
then moveToCurrentRow().
**/
    public void Var078 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT ID, VALUE FROM "
                                                        + JDRSTest.RSTEST_POS + " FOR UPDATE");
                rs.last ();
                rs.previous ();
                rs.previous ();
                rs.previous ();
                rs.moveToInsertRow ();
                rs.moveToCurrentRow ();
                boolean success = rs.first ();
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
first() - Should clear any warnings.
**/
    @SuppressWarnings("deprecation")
    public void Var079 ()
    {
        if (checkJdbc20 ()) {
            try {
                Statement s = connectionNoPrefetch_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                                                           ResultSet.CONCUR_UPDATABLE);
                ResultSet rs = s.executeQuery ("SELECT C_KEY,C_CHAR_50 FROM " + JDRSTest.RSTEST_GET);
                JDRSTest.position0 (rs, "CHAR_FLOAT");

                // Force a warning (data truncation).
                rs.getBigDecimal("C_CHAR_50", 0);

                SQLWarning before = rs.getWarnings ();
                rs.first ();
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
first() - Set max rows and fetch the rows using first().
Max rows should not affect this.
**/
    public void Var080 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statement2NoPrefetch_.executeQuery ("SELECT * FROM "
                                                         + JDRSTest.RSTEST_POS);
                boolean success = rs.first ();
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
first() - Update the rows using first().
**/
    public void Var081 ()
    {
        if (checkJdbc20 ()) {
          JDSerializeFile serializeFile = null;
          try {
            serializeFile = new JDSerializeFile(connection_, JDRSTest.RSTEST_POS);
                // Update each value.
                ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS + " FOR UPDATE OF VALUE");
                PreparedStatement ps = connectionNoPrefetch_.prepareStatement ("UPDATE "
                                                                     + JDRSTest.RSTEST_POS + " SET VALUE = 'FIRST' WHERE CURRENT OF "
                                                                     + rs.getCursorName ());
                rs.first ();
                ps.execute ();
                rs.close ();
                ps.close ();

                // Go through the result set again as a check.
                // It is okay to just use next() here.
                ResultSet rs2 = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                         + JDRSTest.RSTEST_POS);
                rs2.next ();
                String s1 = rs2.getString (2);
                rs2.close ();

                assertCondition (s1.equals ("FIRST"));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            } finally {
              if (serializeFile != null) {
                try {
                  serializeFile.close();
                } catch (SQLException e) {
                  e.printStackTrace();
                }
              }
            }
        }
    }



// TEST NOTE: It would be nice to verify that first() implicity
//            closes a previously retrieved InputStream.  However
//            it is not obvious how to check that an InputStream
//            has been closed.



/**
isFirst() - Should return false on an empty result set with
no positioning.
**/
    public void Var082 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS + " WHERE ID = -1");
                boolean success = rs.isFirst ();
                rs.close ();
                assertCondition (success == false);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isFirst() - Should return false on an empty result set even after
explicitly positioning the cursor using first().
**/
    public void Var083 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS + " WHERE ID = -1");
                rs.first ();
                boolean success = rs.isFirst ();
                rs.close ();
                assertCondition (success == false);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isFirst() - Should throw an exception on a closed result set.
**/
    public void Var084 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.close ();
                rs.isFirst ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
isFirst() - Should throw an exception on a cancelled statement.
**/
    public void Var085 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                statementNoPrefetch_.cancel ();
                rs.isFirst ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
isFirst() - Should return false on a 1 row result set when positioned
before the only row.
**/
    public void Var086 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS + " WHERE ID = 1");
                boolean success = rs.isFirst ();
                rs.close ();
                assertCondition (success == false);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isFirst() - Should return true on a 1 row result set when positioned
on the only row.
**/
    public void Var087 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS + " WHERE ID = 1");
                rs.next ();
                boolean success = rs.isFirst ();
                rs.close ();
                assertCondition (success == true);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isFirst() - Should return false on a 1 row result set when positioned
after the only row.
**/
    public void Var088()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS + " WHERE ID = 1");
                rs.next ();
                rs.next ();
                boolean success = rs.isFirst ();
                rs.close ();
                assertCondition (success == false);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isFirst() - Should return false on a large result set when
not on the first row.
**/
    public void Var089 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                boolean success = rs.isFirst ();
                rs.close ();
                assertCondition (success == false);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isFirst() - Should return true on a large result set when
positioned to the first row.
**/
    public void Var090 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.first ();
                boolean success = rs.isFirst ();
                rs.close ();
                assertCondition (success == true);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isFirst() - Should return false on a "simple" result set
when not on the first row.
**/
    public void Var091 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = dmdNoPrefetch_.getTableTypes ();
                boolean success = rs.isFirst ();
                rs.close ();
                assertCondition (success == false);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isFirst() - Should return true on a "simple" result set
when positioned to the first row.
**/
    public void Var092 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = dmdNoPrefetch_.getTableTypes ();
                rs.next ();
                boolean success = rs.isFirst ();
                rs.close ();
                assertCondition (success == true);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isFirst() - Should work on a "mapped" result set when not on
the first row.

SQL400 - We can't support scrollable metadata resultsets (CLI/DB restriction).
**/
    public void Var093 ()
    {
        if (isToolboxDriver() && !isSysibmMetadata()) {
            if (checkJdbc20 ()) {
                try {
                    ResultSet rs = dmdNoPrefetch_.getColumns (null, "QIWS",
                                                    "QCUSTCDT", "%");
                    boolean success = rs.isFirst ();
                    rs.close ();
                    assertCondition (success == false);
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
isFirst() - Should work on a "mapped" result set when positioned
to the first row.

SQL400 - We can't support scrollable metadata resultsets (CLI/DB restriction).
**/
    public void Var094 ()
    {
        if (isToolboxDriver() && !isSysibmMetadata()) {
            if (checkJdbc20 ()) {
                try {
                    ResultSet rs = dmdNoPrefetch_.getColumns (null, "QIWS",
                                                    "QCUSTCDT", "%");
                    rs.first ();
                    boolean success = rs.isFirst ();
                    rs.close ();
                    assertCondition (success == true);
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
isFirst() - Should return false after a beforeFirst().
**/
    public void Var095 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.beforeFirst ();
                boolean success = rs.isFirst ();
                rs.close ();
                assertCondition (success == false);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
isFirst() - Should return true after a first().
**/
    public void Var096 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.first ();
                boolean success = rs.isFirst ();
                rs.close ();
                assertCondition (success == true);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
isFirst() - Should return false after an absolute(+) to a
row other than the first.
**/
    public void Var097 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.absolute (50);
                boolean success = rs.isFirst ();
                rs.close ();
                assertCondition (success == false);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
isFirst() - Should return true after an absolute(+) to the
first row.
**/
    public void Var098 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.absolute (1);
                boolean success = rs.isFirst ();
                rs.close ();
                assertCondition (success == true);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
isFirst() - Should return false after an absolute(-) to a
row other than the first.
**/
    public void Var099 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.absolute (-50);
                boolean success = rs.isFirst ();
                rs.close ();
                assertCondition (success == false);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
isFirst() - Should return false after an absolute(-) to the
first row, since the first row can not be checked.
**/
    public void Var100 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.absolute (-99);
                boolean success = rs.isFirst ();
                rs.close ();
                if (isToolboxDriver())
                    assertCondition (success == false);
                else
                    assertCondition (success == true);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isFirst() - Should return false after a relative(+) to a
row other than the first.
**/
    public void Var101 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.next ();
                rs.relative (39);
                boolean success = rs.isFirst ();
                rs.close ();
                assertCondition (success == false);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
isFirst() - Should return true after a relative(+) to the
first row.
**/
    public void Var102 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.next ();
                rs.relative (0);
                boolean success = rs.isFirst ();
                rs.close ();
                assertCondition (success == true);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
isFirst() - Should return false after a relative(-) to a
row other than the first.
**/
    public void Var103 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.last ();
                rs.relative (-50);
                boolean success = rs.isFirst ();
                rs.close ();
                assertCondition (success == false);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
isFirst() - Should return true after a relative(-) to the
first row.
**/
    public void Var104 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.absolute (10);
                rs.relative (-9);
                boolean success = rs.isFirst ();
                rs.close ();
                assertCondition (success == true);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
isFirst() - Should return false after a next() when not on
the first row.
**/
    public void Var105 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.next ();
                rs.next ();
                boolean success = rs.isFirst ();
                rs.close ();
                assertCondition (success == false);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isFirst() - Should return true after a next() when positioned on
the first row.
**/
    public void Var106 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.next ();
                boolean success = rs.isFirst ();
                rs.close ();
                assertCondition (success == true);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isFirst() - Should return false after a previous() when not on
the first row.
**/
    public void Var107 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.absolute (50);
                rs.previous ();
                boolean success = rs.isFirst ();
                rs.close ();
                assertCondition (success == false);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isFirst() - Should return true after a previous() when positioned on
the first row.
**/
    public void Var108 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.absolute (3);
                rs.previous ();
                rs.previous ();
                boolean success = rs.isFirst ();
                rs.close ();
                assertCondition (success == true);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isFirst() - Should return false after a last().
**/
    public void Var109 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.last ();
                boolean success = rs.isFirst ();
                rs.close ();
                assertCondition (success == false);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }




/**
isFirst() - Should return false after an afterLast().
**/
    public void Var110 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                        + JDRSTest.RSTEST_POS);
                rs.afterLast ();
                boolean success = rs.isFirst ();
                rs.close ();
                assertCondition (success == false);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isFirst() - Should return false after a moveToInsertRow().
**/
    public void Var111 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT ID, VALUE FROM "
                                                        + JDRSTest.RSTEST_POS + " FOR UPDATE");
                rs.absolute (-43);
                rs.moveToInsertRow ();
                boolean success = rs.isFirst ();
                rs.close ();
                assertCondition (success == false);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isFirst() - Should return false after a moveToInsertRow(),
then moveToCurrentRow() when not on the first row.
**/
    public void Var112 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT ID, VALUE FROM "
                                                        + JDRSTest.RSTEST_POS + " FOR UPDATE");
                rs.last ();
                rs.previous ();
                rs.previous ();
                rs.previous ();
                rs.moveToInsertRow ();
                rs.moveToCurrentRow ();
                boolean success = rs.isFirst ();
                rs.close ();
                assertCondition (success == false);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
isFirst() - Should return true after a moveToInsertRow(),
then moveToCurrentRow() when not on the first row.
**/
    public void Var113 ()
    {
        if (checkJdbc20 ()) {
            try {
                ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT ID, VALUE FROM "
                                                        + JDRSTest.RSTEST_POS + " FOR UPDATE");
                rs.first ();
                rs.moveToInsertRow ();
                rs.moveToCurrentRow ();
                boolean success = rs.isFirst ();
                rs.close ();
                assertCondition (success == true);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


}



