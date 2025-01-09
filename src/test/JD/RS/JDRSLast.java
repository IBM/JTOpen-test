///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSLast.java
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
import java.util.Hashtable; import java.util.Vector;



/**
Testcase JDRSLast.  This tests the following
methods of the JDBC ResultSet class:

<ul>
<li>last()
<li>isLast()
</ul>
**/
public class JDRSLast
extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSLast";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDRSTest.main(newArgs); 
   }



   // Private data.
   private DatabaseMetaData    dmd_;
   private Statement           statement_;
   private Statement           statement2_;
   private Statement           statement3_;


    private Connection          connectionNoPrefetch_;
    private DatabaseMetaData    dmdNoPrefetch_;
    private Statement           statementNoPrefetch_;
    private Statement           statement2NoPrefetch_;
    private Statement           statement3NoPrefetch_;

/**
Constructor.
**/
   public JDRSLast (AS400 systemObject,
                    Hashtable<String,Vector<String>> namesAndVars,
                    int runMode,
                    FileOutputStream fileOutputStream,
                    
                    String password)
   {
      super (systemObject, "JDRSLast",
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

         // This statement is used for variations that
         // need to test with a forward only result set.
         statement3_ = connection_.createStatement (ResultSet.TYPE_FORWARD_ONLY,
                                                    ResultSet.CONCUR_READ_ONLY);



         connectionNoPrefetch_ = testDriver_.getConnection (baseURL_ + ";data truncation=true", userId_, encryptedPassword_);
         dmdNoPrefetch_ = connectionNoPrefetch_.getMetaData ();
         statementNoPrefetch_ = connectionNoPrefetch_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                                                   ResultSet.CONCUR_UPDATABLE);

         // This statement is used for variations that
         // need to test with the max rows set.
         statement2NoPrefetch_ = connectionNoPrefetch_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                    ResultSet.CONCUR_READ_ONLY);
         statement2NoPrefetch_.setMaxRows (50);

         // This statement is used for variations that
         // need to test with a forward only result set.
         statement3NoPrefetch_ = connectionNoPrefetch_.createStatement (ResultSet.TYPE_FORWARD_ONLY,
                                                    ResultSet.CONCUR_READ_ONLY);





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
         statement3_.close ();
         connection_.close ();

         statementNoPrefetch_.close ();
         statement2NoPrefetch_.close ();
         statement3NoPrefetch_.close ();
         connectionNoPrefetch_.close ();
      }
   }


   protected void cleanupConnections ()
   throws Exception
   {
      if (isJdbc20 ()) {
         connection_.close ();

         connectionNoPrefetch_.close ();
      }
   }



/**
last() - Should return false on an empty result set.
**/
   public void Var001 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS + " WHERE ID = -1");
            boolean success = rs.last ();
            rs.close ();
            assertCondition (success == false);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
last() - Should throw an exception on a closed result set.
**/
   public void Var002 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS);
            rs.close ();
            rs.last ();
            failed ("Didn't throw SQLException");
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
last() - Should throw an exception on a cancelled statement.
**/
   public void Var003 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS);
            statement_.cancel ();
            rs.last ();
            failed ("Didn't throw SQLException");
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
last() - Should throw an exception on a foward only result set.
**/
   public void Var004 ()
   {
      if (checkJdbc20 ()) {
         try {
            Statement s = connection_.createStatement (ResultSet.TYPE_FORWARD_ONLY,
                                                       ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = s.executeQuery ("SELECT * FROM "
                                           + JDRSTest.RSTEST_POS);
            rs.last ();
            failed ("Didn't throw SQLException");
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
last() - Should work on a 1 row result set.
**/
   public void Var005 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS + " WHERE ID = 1");
            boolean success = rs.last ();
            int id1 = rs.getInt (1);
            rs.close ();
            assertCondition ((success == true) && (id1 == 1));
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
last() - Should work on a large result set.
**/
   public void Var006 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS);
            boolean success = rs.last ();
            int id1 = rs.getInt (1);
            rs.close ();
            assertCondition ((success == true) && (id1 == 99));
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
last() - Should work on a "simple" result set.
**/
   public void Var007 ()
   {
      if (checkJdbc20 ()) {
         try {
                ResultSet rs ;

		if (getRelease() >= JDTestDriver.RELEASE_V7R1M0 || isJdbc40()) {
		    rs = statement2_.executeQuery("select * from SYSIBM.SYSTBLTYPE"); 
		} else { 

		    rs = dmd_.getTableTypes ();
		}
            boolean success = rs.last ();
            String s1 = rs.getString ("TABLE_TYPE");
            rs.close ();
            assertCondition ((success == true) && (s1 != null));
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
last() - Should work on a "mapped" result set.

SQL400 - We can't support scrollable metadata resultsets (CLI/DB restriction).
**/
   public void Var008 ()
   {
      if (checkJdbc20 ()) {

         if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
            notApplicable ("Native driver doesn't support scrollable metadata result sets.");
            return;
         }
         if (isToolboxDriver() ) {
             notApplicable ("metadata doesn't support scrollable metadata result sets.");
             return;
          }

         try {
            ResultSet rs = dmd_.getColumns (null, "QIWS",
                                            "QCUSTCDT", "%");
            boolean success = rs.last ();
            String s1 = rs.getString ("TABLE_NAME");
            rs.close ();
            assertCondition ((success == true) && (s1.startsWith ("QCUSTCDT")));
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
last() - Should return true after a beforeFirst().
**/
   public void Var009 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS);
            rs.beforeFirst ();
            boolean success = rs.last ();
            int id1 = rs.getInt (1);
            rs.close ();
            assertCondition ((success == true) && (id1 == 99));
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }




/**
last() - Should return true after a first().
**/
   public void Var010 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS);
            rs.first ();
            boolean success = rs.last ();
            int id1 = rs.getInt (1);
            rs.close ();
            assertCondition ((success == true) && (id1 == 99));
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }




/**
last() - Should return true after an absolute().
**/
   public void Var011 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS);
            rs.absolute (50);
            boolean success = rs.last ();
            int id1 = rs.getInt (1);
            rs.close ();
            assertCondition ((success == true) && (id1 == 99));
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }




/**
last() - Should return true after a relative().
**/
   public void Var012 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS);
            rs.relative (75);
            boolean success = rs.last ();
            int id1 = rs.getInt (1);
            rs.close ();
            assertCondition ((success == true) && (id1 == 99));
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }




/**
last() - Should return true after a next().
**/
   public void Var013 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS);
            rs.next ();
            boolean success = rs.last ();
            int id1 = rs.getInt (1);
            rs.close ();
            assertCondition ((success == true) && (id1 == 99));
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
last() - Should return true after a previous().
**/
   public void Var014 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statement_.executeQuery ("SELECT ID, VALUE FROM "
                                                    + JDRSTest.RSTEST_POS);
            rs.last ();
            rs.previous ();
            boolean success = rs.last ();
            int id1 = rs.getInt ("ID");
            rs.close ();
            assertCondition ((success == true) && (id1 == 99));
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
last() - Should return true after a last().
**/
   public void Var015 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS);
            rs.last ();
            boolean success = rs.last ();
            int id1 = rs.getInt (1);
            rs.close ();
            assertCondition ((success == true) && (id1 == 99));
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }




/**
last() - Should return true after an afterLast().
**/
   public void Var016 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS);
            rs.afterLast ();
            boolean success = rs.last ();
            int id1 = rs.getInt (1);
            rs.close ();
            assertCondition ((success == true) && (id1 == 99));
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
last() - Should return true after a moveToInsertRow().
**/
   public void Var017 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statement_.executeQuery ("SELECT ID, VALUE FROM "
                                                    + JDRSTest.RSTEST_POS + " FOR UPDATE");
            rs.absolute (-43);
            rs.moveToInsertRow ();
            boolean success = rs.last ();
            int id1 = rs.getInt (1);
            rs.close ();
            assertCondition ((success == true) && (id1 == 99));
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
last() - Should return true after a moveToInsertRow(),
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
            boolean success = rs.last ();
            int id1 = rs.getInt (1);
            rs.close ();
            assertCondition ((success == true) && (id1 == 99));
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
last() - Should clear any warnings.
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
            rs.last ();
            SQLWarning after = rs.getWarnings ();
            rs.close ();
            s.close ();
            assertCondition ((before != null) && (after == null));
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
last() - Set max rows and fetch the rows using last().
Max rows should not affect this.  For the native JDBC driver, 
max rows does effect this.
**/
   public void Var020 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statement2_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_POS);
            boolean success = rs.last ();
            int id1 = rs.getInt (1);
            rs.close ();

            assertCondition ((success == true) && (id1 == 50));
         } 
         catch (Exception e) 
         {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
last() - Update the rows using last().
**/
   public void Var021 ()
   {
      if (checkJdbc20 ()) {
         try {
            // Update each value.
            ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS + " FOR UPDATE OF VALUE");
            PreparedStatement ps = connection_.prepareStatement ("UPDATE "
                                                                 + JDRSTest.RSTEST_POS + " SET VALUE = 'LAST' WHERE CURRENT OF "
                                                                 + rs.getCursorName ());
            rs.last ();
            ps.execute ();
            rs.close ();
            ps.close ();

            // Go through the result set again as a check.
            // It is okay to just use next() here.
            ResultSet rs2 = statement_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_POS);
            for (int i = 1; i <= 99; ++i)
               rs2.next ();
            String s1 = rs2.getString (2);
            rs2.close ();

            assertCondition (s1.equals ("LAST"));
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



// TEST NOTE: It would be nice to verify that last() implicity
//            closes a previously retrieved InputStream.  However
//            it is not obvious how to check that an InputStream
//            has been closed.



/**
isLast() - Should return false on an empty result set with
no positioning.
**/
   public void Var022 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS + " WHERE ID = -1");
            boolean success = rs.isLast ();
            rs.close ();
            assertCondition (success == false);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
isLast() - Should return false on an empty result set even after
explicitly positioning the cursor using last().
**/
   public void Var023 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS + " WHERE ID = -1");
            rs.last ();
            boolean success = rs.isLast ();
            rs.close ();
            assertCondition (success == false);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
isLast() - Should throw an exception on a closed result set.
**/
   public void Var024 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS);
            rs.close ();
            rs.isLast ();
            failed ("Didn't throw SQLException");
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
isLast() - Should throw an exception on a cancelled statement.
**/
   public void Var025 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS);
            statement_.cancel ();
            rs.isLast ();
            failed ("Didn't throw SQLException");
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
isLast() - Should return false on a 1 row result set when positioned
before the only row.
**/
   public void Var026 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS + " WHERE ID = 1");
            boolean success = rs.isLast ();
            rs.close ();
            assertCondition (success == false);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
isLast() - Should return true on a 1 row result set when positioned
on the only row.
**/
   public void Var027 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS + " WHERE ID = 1");
            rs.afterLast ();
            rs.previous ();
            boolean success = rs.isLast ();
            rs.close ();
            assertCondition (success == true);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
isLast() - Should return false on a 1 row result set when positioned
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
            boolean success = rs.isLast ();
            rs.close ();
            assertCondition (success == false);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
isLast() - Should return false on a large result set when
not on the last row.
**/
   public void Var029 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS);
            boolean success = rs.isLast ();
            rs.close ();
            assertCondition (success == false);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
isLast() - Should return true on a large result set when
positioned to the last row.
**/
   public void Var030 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS);
            rs.last ();
            boolean success = rs.isLast ();
            rs.close ();
            assertCondition (success == true);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
isLast() - Should return false on a "simple" result set
when not on the last row.
**/
   public void Var031 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = dmd_.getTableTypes ();
            boolean success = rs.isLast ();
            rs.close ();
            assertCondition (success == false);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
isLast() - Should return true on a "simple" result set
when positioned to the last row.
**/
   public void Var032 ()
   {
      if (checkJdbc20 ()) {
         try {
                ResultSet rs ;

		if (getRelease() >= JDTestDriver.RELEASE_V7R1M0 || isJdbc40()) {
		    rs = statement2_.executeQuery("select * from SYSIBM.SYSTBLTYPE"); 
		} else { 

		 rs  = dmd_.getTableTypes ();
		}
            rs.last ();
            boolean success = rs.isLast ();
            rs.close ();
            assertCondition (success == true);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
isLast() - Should work on a "mapped" result set when not on
the last row.

SQL400 - We can't support scrollable metadata resultsets (CLI/DB restriction).
**/
   public void Var033 ()
   {
      if (checkJdbc20 ()) {

         if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
            notApplicable ("Native driver doesn't support scrollable metadata result sets.");
            return;
         }
         if (isToolboxDriver() && !isSysibmMetadata()) {
             notApplicable ("sysibm metadata doesn't support scrollable metadata result sets.");
             return;
          }
         try {
            ResultSet rs = dmd_.getColumns (null, "QIWS",
                                            "QCUSTCDT", "%");
            boolean success = rs.isLast ();
            rs.close ();
            assertCondition (success == false);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
isLast() - Should work on a "mapped" result set when positioned
to the last row.

SQL400 - We can't support scrollable metadata resultsets (CLI/DB restriction).
**/
   public void Var034 ()
   {
      if (checkJdbc20 ()) {

         if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
            notApplicable ("Native driver doesn't support scrollable metadata result sets.");
            return;
         }       
         if (isToolboxDriver() ) {
             notApplicable ("metadata doesn't support scrollable metadata result sets.");
             return;
          }

         try {
            ResultSet rs = dmd_.getColumns (null, "QIWS",
                                            "QCUSTCDT", "%");
            rs.last ();
            boolean success = rs.isLast ();
            rs.close ();
            assertCondition (success == true);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
isLast() - Should return false after a beforeFirst().
**/
   public void Var035 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS);
            rs.beforeFirst ();
            boolean success = rs.isLast ();
            rs.close ();
            assertCondition (success == false);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }




/**
isLast() - Should return false after a first().
**/
   public void Var036 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS);
            rs.first ();
            boolean success = rs.isLast ();
            rs.close ();
            assertCondition (success == false);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }




/**
isLast() - Should return false after an absolute(+) to a
row other than the last.
**/
   public void Var037 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS);
            rs.absolute (50);
            boolean success = rs.isLast ();
            rs.close ();
            assertCondition (success == false);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }




/**
isLast() - Should return false after an absolute(+) to the
last row, since this can not be determined.  The native JDBC
driver can determine this and will return true here.
**/
   public void Var038 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS);
            rs.absolute (99);
            boolean success = rs.isLast ();
            rs.close ();
            assertCondition (success == true);
         } 
         catch (Exception e) 
         {
            failed (e, "Unexpected Exception");
         }
      }
   }




/**
isLast() - Should return false after an absolute(-) to a
row other than the last.
**/
   public void Var039 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS);
            rs.absolute (-50);
            boolean success = rs.isLast ();
            rs.close ();
            assertCondition (success == false);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }




/**
isLast() - Should return true after an absolute(-) to the
last row.
**/
   public void Var040 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS);
            rs.absolute (-1);
            boolean success = rs.isLast ();
            rs.close ();
            assertCondition (success == true);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }




/**
isLast() - Should return false after a relative(+) to a
row other than the last.
**/
   public void Var041 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS);
            rs.relative (39);
            boolean success = rs.isLast ();
            rs.close ();
            assertCondition (success == false);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }




/**
isLast() - Should return true after a relative(+) to the
last row.
**/
   public void Var042 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS);
            rs.absolute (-10);
            rs.relative (9);
            boolean success = rs.isLast ();
            rs.close ();
            assertCondition (success == true);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }




/**
isLast() - Should return false after a relative(-) to a
row other than the last.
**/
   public void Var043 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS);
            rs.last ();
            rs.relative (-50);
            boolean success = rs.isLast ();
            rs.close ();
            assertCondition (success == false);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }




/**
isLast() - Should return false after a next() when not on
the last row.
**/
   public void Var044 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS);
            rs.next ();
            boolean success = rs.isLast ();
            rs.close ();
            assertCondition (success == false);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
isLast() - Should return true after a next() when positioned on
the last row.
**/
   public void Var045 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS);
            rs.absolute (-2);
            rs.next ();
            boolean success = rs.isLast ();
            rs.close ();
            assertCondition (success == true);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
isLast() - Should return false after a previous() when not on
the last row.
**/
   public void Var046 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS);
            rs.absolute (50);
            rs.previous ();
            boolean success = rs.isLast ();
            rs.close ();
            assertCondition (success == false);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
isLast() - Should return true after a previous() when positioned on
the last row.
**/
   public void Var047 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS);
            rs.afterLast ();
            rs.previous ();
            boolean success = rs.isLast ();
            rs.close ();
            assertCondition (success == true);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
isLast() - Should return true after a last().
**/
   public void Var048 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS);
            rs.last ();
            boolean success = rs.isLast ();
            rs.close ();
            assertCondition (success == true);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }




/**
isLast() - Should return false after an afterLast().
**/
   public void Var049 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS);
            rs.afterLast ();
            boolean success = rs.isLast ();
            rs.close ();
            assertCondition (success == false);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
isLast() - Should return false after a moveToInsertRow().
**/
   public void Var050 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statement_.executeQuery ("SELECT ID, VALUE FROM "
                                                    + JDRSTest.RSTEST_POS + " FOR UPDATE");
            rs.absolute (-43);
            rs.moveToInsertRow ();
            boolean success = rs.isLast ();
            rs.close ();
            assertCondition (success == false);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
isLast() - Should return false after a moveToInsertRow(),
then moveToCurrentRow() when not on the last row.
**/
   public void Var051 ()
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
            boolean success = rs.isLast ();
            rs.close ();
            assertCondition (success == false);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
isLast() - Should return true after a moveToInsertRow(),
then moveToCurrentRow() when not on the last row.
**/
   public void Var052 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statement_.executeQuery ("SELECT ID, VALUE FROM "
                                                    + JDRSTest.RSTEST_POS + " FOR UPDATE");
            rs.last ();
            rs.moveToInsertRow ();
            rs.moveToCurrentRow ();
            boolean success = rs.isLast ();
            rs.close ();
            assertCondition (success == true);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
isLast() - Set max rows and fetch the rows using next(),
Verify that max rows is honored.
**/
   public void Var053 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statement2_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_POS);
            for (int i = 1; i <= 50; ++i)
               rs.next ();
            boolean success = rs.isLast ();
            rs.close ();
            assertCondition (success == true);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
isLast() - Set max rows and fetch the rows using absolute(-),
which makes us lose track of max rows.  Verify that max rows
is not honored.
**/
   public void Var054 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statement2_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_POS);
            rs.absolute (-49);
            boolean success = rs.isLast ();
            rs.close ();
            assertCondition (success == false);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }


/**
isLast() -- Should return false where there is still more data.
                 Also verify that the result set is correct when isBeforeLast is used when
                 prefetch is off
**/

    public void Var055 ()
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

		    boolean Last1 = rs.isLast();
		    if (Last1) { passed=false; sb.append("Last1 returned "+Last1+"\n"); }

		    String  string1 = rs.getString(1);
		    if (! "A".equals(string1)) { passed = false; sb.append("Expected 'A' got '"+string1+"'\n");}
 

		    boolean fetch2 = rs.next();
		    if (!fetch2) { passed=false; sb.append("fetch2 returned "+fetch2+"\n"); }

		    boolean Last2 = rs.isLast();
		    if (Last2) { passed=false; sb.append("Last2 returned "+Last2+"\n"); }

		    String  string2 = rs.getString(1);
		    if (! "B".equals(string2)) { passed = false; sb.append("Expected 'B' got '"+string2+"'\n");}
 

		    boolean fetch3 = rs.next();
		    if (!fetch3) { passed=false; sb.append("fetch3 returned "+fetch3+"\n"); }

		    boolean Last3 = rs.isLast();
		    if (!Last3) { passed=false; sb.append("Last3 returned "+Last3+"\n"); }

		    String  string3 = rs.getString(1);
		    if (! "C".equals(string3)) { passed = false; sb.append("Expected 'C' got '"+string3+"'\n");}


		    boolean fetch4 = rs.next();
		    if (fetch4) { passed=false; sb.append("fetch4 returned "+fetch4+"\n"); }

		    boolean Last4 = rs.isLast();
		    if (Last4) { passed=false; sb.append("Last4 returned "+Last4+"\n"); }

		    assertCondition (passed, sb.toString() +" -- Added by native driver 11/29/2010");
		} catch (Exception e) {
		    failed (e, "Unexpected Exception sql="+sql+" --  added by native driver (11/29/2010)");
		}
	    }
	}
    }

    public void Var056 () { notApplicable(); }
    public void Var057 () { notApplicable(); }
    public void Var058 () { notApplicable(); }
    public void Var059 () { notApplicable(); }
    public void Var060 () { notApplicable(); }

// New Prefetch Testcases

/**
last() - Should return false on an empty result set.
**/
   public void Var061 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS + " WHERE ID = -1");
            boolean success = rs.last ();
            rs.close ();
            assertCondition (success == false);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
last() - Should throw an exception on a closed result set.
**/
   public void Var062 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS);
            rs.close ();
            rs.last ();
            failed ("Didn't throw SQLException");
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
last() - Should throw an exception on a cancelled statement.
**/
   public void Var063 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS);
            statementNoPrefetch_.cancel ();
            rs.last ();
            failed ("Didn't throw SQLException");
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
last() - Should throw an exception on a foward only result set.
**/
   public void Var064 ()
   {
      if (checkJdbc20 ()) {
         try {
            Statement s = connectionNoPrefetch_.createStatement (ResultSet.TYPE_FORWARD_ONLY,
                                                       ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = s.executeQuery ("SELECT * FROM "
                                           + JDRSTest.RSTEST_POS);
            rs.last ();
            failed ("Didn't throw SQLException");
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
last() - Should work on a 1 row result set.
**/
   public void Var065 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS + " WHERE ID = 1");
            boolean success = rs.last ();
            int id1 = rs.getInt (1);
            rs.close ();
            assertCondition ((success == true) && (id1 == 1));
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
last() - Should work on a large result set.
**/
   public void Var066 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS);
            boolean success = rs.last ();
            int id1 = rs.getInt (1);
            rs.close ();
            assertCondition ((success == true) && (id1 == 99));
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
last() - Should work on a "simple" result set.
**/
   public void Var067 ()
   {
      if (checkJdbc20 ()) {
         try {
                ResultSet rs ;

		if (getRelease() >= JDTestDriver.RELEASE_V7R1M0 || isJdbc40()) {
		    rs = statement2NoPrefetch_.executeQuery("select * from SYSIBM.SYSTBLTYPE"); 
		} else { 

		    rs = dmdNoPrefetch_.getTableTypes ();
		}
            boolean success = rs.last ();
            String s1 = rs.getString ("TABLE_TYPE");
            rs.close ();
            assertCondition ((success == true) && (s1 != null));
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
last() - Should work on a "mapped" result set.

SQL400 - We can't support scrollable metadata resultsets (CLI/DB restriction).
**/
   public void Var068 ()
   {
      if (checkJdbc20 ()) {

         if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
            notApplicable ("Native driver doesn't support scrollable metadata result sets.");
            return;
         }
         if (isToolboxDriver() ) {
             notApplicable ("metadata doesn't support scrollable metadata result sets.");
             return;
          }

         try {
            ResultSet rs = dmdNoPrefetch_.getColumns (null, "QIWS",
                                            "QCUSTCDT", "%");
            boolean success = rs.last ();
            String s1 = rs.getString ("TABLE_NAME");
            rs.close ();
            assertCondition ((success == true) && (s1.startsWith ("QCUSTCDT")));
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
last() - Should return true after a beforeFirst().
**/
   public void Var069 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS);
            rs.beforeFirst ();
            boolean success = rs.last ();
            int id1 = rs.getInt (1);
            rs.close ();
            assertCondition ((success == true) && (id1 == 99));
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }




/**
last() - Should return true after a first().
**/
   public void Var070 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS);
            rs.first ();
            boolean success = rs.last ();
            int id1 = rs.getInt (1);
            rs.close ();
            assertCondition ((success == true) && (id1 == 99));
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }




/**
last() - Should return true after an absolute().
**/
   public void Var071 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS);
            rs.absolute (50);
            boolean success = rs.last ();
            int id1 = rs.getInt (1);
            rs.close ();
            assertCondition ((success == true) && (id1 == 99));
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }




/**
last() - Should return true after a relative().
**/
   public void Var072 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS);
            rs.relative (75);
            boolean success = rs.last ();
            int id1 = rs.getInt (1);
            rs.close ();
            assertCondition ((success == true) && (id1 == 99));
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }




/**
last() - Should return true after a next().
**/
   public void Var073 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS);
            rs.next ();
            boolean success = rs.last ();
            int id1 = rs.getInt (1);
            rs.close ();
            assertCondition ((success == true) && (id1 == 99));
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
last() - Should return true after a previous().
**/
   public void Var074 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT ID, VALUE FROM "
                                                    + JDRSTest.RSTEST_POS);
            rs.last ();
            rs.previous ();
            boolean success = rs.last ();
            int id1 = rs.getInt ("ID");
            rs.close ();
            assertCondition ((success == true) && (id1 == 99));
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
last() - Should return true after a last().
**/
   public void Var075 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS);
            rs.last ();
            boolean success = rs.last ();
            int id1 = rs.getInt (1);
            rs.close ();
            assertCondition ((success == true) && (id1 == 99));
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }




/**
last() - Should return true after an afterLast().
**/
   public void Var076 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS);
            rs.afterLast ();
            boolean success = rs.last ();
            int id1 = rs.getInt (1);
            rs.close ();
            assertCondition ((success == true) && (id1 == 99));
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
last() - Should return true after a moveToInsertRow().
**/
   public void Var077 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT ID, VALUE FROM "
                                                    + JDRSTest.RSTEST_POS + " FOR UPDATE");
            rs.absolute (-43);
            rs.moveToInsertRow ();
            boolean success = rs.last ();
            int id1 = rs.getInt (1);
            rs.close ();
            assertCondition ((success == true) && (id1 == 99));
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
last() - Should return true after a moveToInsertRow(),
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
            boolean success = rs.last ();
            int id1 = rs.getInt (1);
            rs.close ();
            assertCondition ((success == true) && (id1 == 99));
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
last() - Should clear any warnings.
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
            rs.last ();
            SQLWarning after = rs.getWarnings ();
            rs.close ();
            s.close ();
            assertCondition ((before != null) && (after == null));
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
last() - Set max rows and fetch the rows using last().
Max rows should not affect this.  For the native JDBC driver, 
max rows does effect this.
**/
   public void Var080 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statement2NoPrefetch_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_POS);
            boolean success = rs.last ();
            int id1 = rs.getInt (1);
            rs.close ();

            assertCondition ((success == true) && (id1 == 50));
         } 
         catch (Exception e) 
         {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
last() - Update the rows using last().
**/
   public void Var081 ()
   {
      if (checkJdbc20 ()) {
         try {
            // Update each value.
            ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS + " FOR UPDATE OF VALUE");
            PreparedStatement ps = connectionNoPrefetch_.prepareStatement ("UPDATE "
                                                                 + JDRSTest.RSTEST_POS + " SET VALUE = 'LAST' WHERE CURRENT OF "
                                                                 + rs.getCursorName ());
            rs.last ();
            ps.execute ();
            rs.close ();
            ps.close ();

            // Go through the result set again as a check.
            // It is okay to just use next() here.
            ResultSet rs2 = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_POS);
            for (int i = 1; i <= 99; ++i)
               rs2.next ();
            String s1 = rs2.getString (2);
            rs2.close ();

            assertCondition (s1.equals ("LAST"));
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



// TEST NOTE: It would be nice to verify that last() implicity
//            closes a previously retrieved InputStream.  However
//            it is not obvious how to check that an InputStream
//            has been closed.



/**
isLast() - Should return false on an empty result set with
no positioning.
**/
   public void Var082 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS + " WHERE ID = -1");
            boolean success = rs.isLast ();
            rs.close ();
            assertCondition (success == false);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
isLast() - Should return false on an empty result set even after
explicitly positioning the cursor using last().
**/
   public void Var083 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS + " WHERE ID = -1");
            rs.last ();
            boolean success = rs.isLast ();
            rs.close ();
            assertCondition (success == false);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
isLast() - Should throw an exception on a closed result set.
**/
   public void Var084 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS);
            rs.close ();
            rs.isLast ();
            failed ("Didn't throw SQLException");
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
isLast() - Should throw an exception on a cancelled statement.
**/
   public void Var085 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS);
            statementNoPrefetch_.cancel ();
            rs.isLast ();
            failed ("Didn't throw SQLException");
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
isLast() - Should return false on a 1 row result set when positioned
before the only row.
**/
   public void Var086 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS + " WHERE ID = 1");
            boolean success = rs.isLast ();
            rs.close ();
            assertCondition (success == false);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
isLast() - Should return true on a 1 row result set when positioned
on the only row.
**/
   public void Var087 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS + " WHERE ID = 1");
            rs.afterLast ();
            rs.previous ();
            boolean success = rs.isLast ();
            rs.close ();
            assertCondition (success == true);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
isLast() - Should return false on a 1 row result set when positioned
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
            boolean success = rs.isLast ();
            rs.close ();
            assertCondition (success == false);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
isLast() - Should return false on a large result set when
not on the last row.
**/
   public void Var089 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS);
            boolean success = rs.isLast ();
            rs.close ();
            assertCondition (success == false);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
isLast() - Should return true on a large result set when
positioned to the last row.
**/
   public void Var090 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS);
            rs.last ();
            boolean success = rs.isLast ();
            rs.close ();
            assertCondition (success == true);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
isLast() - Should return false on a "simple" result set
when not on the last row.
**/
   public void Var091 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = dmdNoPrefetch_.getTableTypes ();
            boolean success = rs.isLast ();
            rs.close ();
            assertCondition (success == false);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
isLast() - Should return true on a "simple" result set
when positioned to the last row.
**/
   public void Var092 ()
   {
      if (checkJdbc20 ()) {
         try {
                ResultSet rs ;

		if (getRelease() >= JDTestDriver.RELEASE_V7R1M0 || isJdbc40()) {
		    rs = statement2NoPrefetch_.executeQuery("select * from SYSIBM.SYSTBLTYPE"); 
		} else { 

		 rs  = dmdNoPrefetch_.getTableTypes ();
		}
            rs.last ();
            boolean success = rs.isLast ();
            rs.close ();
            assertCondition (success == true);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
isLast() - Should work on a "mapped" result set when not on
the last row.

SQL400 - We can't support scrollable metadata resultsets (CLI/DB restriction).
**/
   public void Var093 ()
   {
      if (checkJdbc20 ()) {

         if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
            notApplicable ("Native driver doesn't support scrollable metadata result sets.");
            return;
         }
         if (isToolboxDriver() && !isSysibmMetadata()) {
             notApplicable ("sysibm metadata doesn't support scrollable metadata result sets.");
             return;
          }
         try {
            ResultSet rs = dmdNoPrefetch_.getColumns (null, "QIWS",
                                            "QCUSTCDT", "%");
            boolean success = rs.isLast ();
            rs.close ();
            assertCondition (success == false);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
isLast() - Should work on a "mapped" result set when positioned
to the last row.

SQL400 - We can't support scrollable metadata resultsets (CLI/DB restriction).
**/
   public void Var094 ()
   {
      if (checkJdbc20 ()) {

         if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
            notApplicable ("Native driver doesn't support scrollable metadata result sets.");
            return;
         }       
         if (isToolboxDriver() ) {
             notApplicable ("metadata doesn't support scrollable metadata result sets.");
             return;
          }

         try {
            ResultSet rs = dmdNoPrefetch_.getColumns (null, "QIWS",
                                            "QCUSTCDT", "%");
            rs.last ();
            boolean success = rs.isLast ();
            rs.close ();
            assertCondition (success == true);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
isLast() - Should return false after a beforeFirst().
**/
   public void Var095 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS);
            rs.beforeFirst ();
            boolean success = rs.isLast ();
            rs.close ();
            assertCondition (success == false);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }




/**
isLast() - Should return false after a first().
**/
   public void Var096 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS);
            rs.first ();
            boolean success = rs.isLast ();
            rs.close ();
            assertCondition (success == false);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }




/**
isLast() - Should return false after an absolute(+) to a
row other than the last.
**/
   public void Var097 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS);
            rs.absolute (50);
            boolean success = rs.isLast ();
            rs.close ();
            assertCondition (success == false);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }




/**
isLast() - Should return false after an absolute(+) to the
last row, since this can not be determined.  The native JDBC
driver can determine this and will return true here.
**/
   public void Var098 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS);
            rs.absolute (99);
            boolean success = rs.isLast ();
            rs.close ();
            assertCondition (success == true);
         } 
         catch (Exception e) 
         {
            failed (e, "Unexpected Exception");
         }
      }
   }




/**
isLast() - Should return false after an absolute(-) to a
row other than the last.
**/
   public void Var099 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS);
            rs.absolute (-50);
            boolean success = rs.isLast ();
            rs.close ();
            assertCondition (success == false);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }




/**
isLast() - Should return true after an absolute(-) to the
last row.
**/
   public void Var100 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS);
            rs.absolute (-1);
            boolean success = rs.isLast ();
            rs.close ();
            assertCondition (success == true);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }




/**
isLast() - Should return false after a relative(+) to a
row other than the last.
**/
   public void Var101 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS);
            rs.relative (39);
            boolean success = rs.isLast ();
            rs.close ();
            assertCondition (success == false);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }




/**
isLast() - Should return true after a relative(+) to the
last row.
**/
   public void Var102 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS);
            rs.absolute (-10);
            rs.relative (9);
            boolean success = rs.isLast ();
            rs.close ();
            assertCondition (success == true);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }




/**
isLast() - Should return false after a relative(-) to a
row other than the last.
**/
   public void Var103 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS);
            rs.last ();
            rs.relative (-50);
            boolean success = rs.isLast ();
            rs.close ();
            assertCondition (success == false);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }




/**
isLast() - Should return false after a next() when not on
the last row.
**/
   public void Var104 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS);
            rs.next ();
            boolean success = rs.isLast ();
            rs.close ();
            assertCondition (success == false);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
isLast() - Should return true after a next() when positioned on
the last row.
**/
   public void Var105 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS);
            rs.absolute (-2);
            rs.next ();
            boolean success = rs.isLast ();
            rs.close ();
            assertCondition (success == true);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
isLast() - Should return false after a previous() when not on
the last row.
**/
   public void Var106 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS);
            rs.absolute (50);
            rs.previous ();
            boolean success = rs.isLast ();
            rs.close ();
            assertCondition (success == false);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
isLast() - Should return true after a previous() when positioned on
the last row.
**/
   public void Var107 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS);
            rs.afterLast ();
            rs.previous ();
            boolean success = rs.isLast ();
            rs.close ();
            assertCondition (success == true);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
isLast() - Should return true after a last().
**/
   public void Var108 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS);
            rs.last ();
            boolean success = rs.isLast ();
            rs.close ();
            assertCondition (success == true);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }




/**
isLast() - Should return false after an afterLast().
**/
   public void Var109 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS);
            rs.afterLast ();
            boolean success = rs.isLast ();
            rs.close ();
            assertCondition (success == false);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
isLast() - Should return false after a moveToInsertRow().
**/
   public void Var110 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT ID, VALUE FROM "
                                                    + JDRSTest.RSTEST_POS + " FOR UPDATE");
            rs.absolute (-43);
            rs.moveToInsertRow ();
            boolean success = rs.isLast ();
            rs.close ();
            assertCondition (success == false);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
isLast() - Should return false after a moveToInsertRow(),
then moveToCurrentRow() when not on the last row.
**/
   public void Var111 ()
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
            boolean success = rs.isLast ();
            rs.close ();
            assertCondition (success == false);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
isLast() - Should return true after a moveToInsertRow(),
then moveToCurrentRow() when not on the last row.
**/
   public void Var112 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statementNoPrefetch_.executeQuery ("SELECT ID, VALUE FROM "
                                                    + JDRSTest.RSTEST_POS + " FOR UPDATE");
            rs.last ();
            rs.moveToInsertRow ();
            rs.moveToCurrentRow ();
            boolean success = rs.isLast ();
            rs.close ();
            assertCondition (success == true);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
isLast() - Set max rows and fetch the rows using next(),
Verify that max rows is honored.
**/
   public void Var113 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statement2NoPrefetch_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_POS);
            for (int i = 1; i <= 50; ++i)
               rs.next ();
            boolean success = rs.isLast ();
            rs.close ();
            assertCondition (success == true);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
isLast() - Set max rows and fetch the rows using absolute(-),
which makes us lose track of max rows.  Verify that max rows
is not honored.
**/
   public void Var114 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statement2NoPrefetch_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_POS);
            rs.absolute (-49);
            boolean success = rs.isLast ();
            rs.close ();
            assertCondition (success == false);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }
   

}



