///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSRefreshRow.java
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
import java.sql.SQLException;

import java.sql.ResultSet;

import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.Hashtable;



/**
Testcase JDRSRefreshRow.  This tests the following
methods of the JDBC ResultSet class:

<ul>
<li>refreshRow()
</ul>
**/
public class JDRSRefreshRow
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSRefreshRow";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDRSTest.main(newArgs); 
   }



   // Private data.
   private DatabaseMetaData    dmd_;
   private Statement           statement_;
   private Statement           statement3_;



/**
Constructor.
**/
   public JDRSRefreshRow (AS400 systemObject,
                          Hashtable namesAndVars,
                          int runMode,
                          FileOutputStream fileOutputStream,
                          
                          String password)
   {
      super (systemObject, "JDRSRefreshRow",
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

         // This statement is forward only.
         statement_ = connection_.createStatement (ResultSet.TYPE_FORWARD_ONLY,
                                                   ResultSet.CONCUR_READ_ONLY);

         // This statement is used for variations that
         // need to test with a scrollable cursor.
	 try {
	     statement3_ = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_UPDATABLE);
	 } catch (SQLException e ) {
	     statement3_ = connection_.createStatement ();
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
         statement_.close ();
         statement3_.close ();
         connection_.close ();
      }
   }



/**
refreshRow() - Should throw an exception on a closed result set.
**/
   public void Var001 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statement3_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_POS);
            rs.close ();
            rs.refreshRow ();
            failed ("Didn't throw SQLException");
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
refreshRow() - Should throw an exception on a cancelled statement.
**/
   public void Var002 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statement3_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_POS);
            statement3_.cancel ();
            rs.refreshRow ();
            failed ("Didn't throw SQLException");
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
refreshRow() - Should throw an exception on a forward only result set.
**/
   public void Var003 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statement_.executeQuery ("SELECT * FROM "
                                                    + JDRSTest.RSTEST_POS);
            rs.next ();
            rs.refreshRow ();
            failed ("Didn't throw SQLException");
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
refreshRow() - Should work on a 1 row result set.
**/
   public void Var004 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statement3_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_POS + " WHERE ID = 1");
            rs.next ();
            rs.refreshRow ();
            int id = rs.getInt ("ID");
            boolean success = rs.next ();
            rs.close ();
            assertCondition ((success == false) && (id == 1));
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
refreshRow() - Should work on a large result set.
**/
   public void Var005 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statement3_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_POS);
            boolean success = true;
            int count = 0;
            while (rs.next ()) {
               rs.refreshRow ();
               ++count;
               if (rs.getInt (1) != count)
                  success = false;
            }
            rs.close ();
            assertCondition ((success == true) && (count == 99));
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
refreshRow() - Should work on a "simple" result set.
**/
   public void Var006 ()
   {
      if (checkJdbc20 ()) {
         try {
                ResultSet rs ;

		if (getRelease() >= JDTestDriver.RELEASE_V7R1M0 || isJdbc40()) {
                    // Cursor must be scrollable
		    rs = statement3_.executeQuery("select * from SYSIBM.SYSTBLTYPE");
		} else {
		    rs = dmd_.getTableTypes ();
		}
            boolean success = true;
            int count = 0;
            while (rs.next ()) {
               rs.refreshRow ();
               ++count;
               if (rs.getString ("TABLE_TYPE") == null)
                  success = false;
            }
            rs.close ();

	    /* Changed the comparision -- @D3C */
	    int expectedCount = 3;
	    if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
		// add 1 for MQT
		expectedCount++;
	    }
	    if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
		// add 1 for ALIAS
		expectedCount++;
	    }

	    assertCondition ((success == true) && (count == expectedCount), "Changed 08/03/05 by native driver, success="+success+" count = "+count+" sb "+expectedCount );

/*
//            if(getRelease() < JDTestDriver.RELEASE_V5R3M0)          //@D1A
	    if(getRelease() < JDTestDriver.RELEASE_V5R3M0 ||		// @D2
	       getDriver() == JDTestDriver.DRIVER_NATIVE )		// @D2
                assertCondition ((success == true) && (count == 3));
            else                                                    //@D1A
                assertCondition ((success == true) && (count == 4));//@D1A
*/
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
refreshRow() - Should work on a "mapped" result set.

SQL400 - We can't support scrollable metadata resultsets (CLI/DB restriction).
**/
   public void Var007 ()
   {
      if (isToolboxDriver() && !isSysibmMetadata()) {
         if (checkJdbc20 ()) {
            try {
               ResultSet rs = dmd_.getColumns (null, "QIWS",
                                               "QCUSTCDT", "%");
               boolean success = true;
               int count = 0;
               while (rs.next ()) {
                  rs.refreshRow ();
                  ++count;
                  if (! rs.getString ("TABLE_NAME").startsWith ("QCUSTCDT"))
                     success = false;
               }
               rs.close ();
               assertCondition ((success == true) && (count == 11));
            } catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
         }
      } else {
         notApplicable();
      }
   }



/**
refreshRow() - Should throw an exception after a beforeFirst().
**/
   public void Var008 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statement3_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_POS);
            rs.beforeFirst ();
            rs.refreshRow ();
            failed ("Didn't throw SQLException");
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }




/**
refreshRow() - Should refresh the row after a first().
**/
   public void Var009 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statement3_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_POS);
            rs.first ();
            rs.refreshRow ();
            int id = rs.getInt (1);
            rs.close ();
            assertCondition (id == 1);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }




/**
refreshRow() - Should refresh the row after an absolute().
**/
   public void Var010 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statement3_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_POS);
            rs.absolute (50);
            rs.refreshRow ();
            int id = rs.getInt ("ID");
            rs.close ();
            assertCondition (id == 50);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }




/**
refreshRow() - Should refresh the row after a relative().
**/
   public void Var011 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statement3_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_POS);
            rs.first ();
            rs.relative (75);
            rs.refreshRow ();
            int id = rs.getInt ("ID");
            rs.close ();
            assertCondition (id == 76);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }




/**
refreshRow() - Should refresh the row after a previous().
**/
   public void Var012 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statement3_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_POS);
            rs.absolute (75);
            rs.previous ();
            rs.refreshRow ();
            int id = rs.getInt (1);
            rs.close ();
            assertCondition (id == 74);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
refreshRow() - Should refresh the row after a last().
**/
   public void Var013 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statement3_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_POS);
            rs.last ();
            rs.refreshRow ();
            int id = rs.getInt (1);
            rs.close ();
            assertCondition (id == 99);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }




/**
refreshRow() - Should throw an exception after an afterLast().
**/
   public void Var014 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statement3_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_POS);
            rs.afterLast ();
            rs.refreshRow ();
            failed ("Didn't throw SQLException");
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
refreshRow() - Should throw an exception a moveToInsertRow().
**/
   public void Var015 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statement3_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_POS + " FOR UPDATE");
            rs.next ();
            rs.moveToInsertRow ();
            rs.refreshRow ();
            failed ("Didn't throw SQLException");
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
refreshRow() - Should refresh the row after a moveToInsertRow(),
then moveToCurrentRow().
**/
   public void Var016 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statement3_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_POS + " FOR UPDATE");
            rs.absolute (33);
            rs.moveToInsertRow ();
            rs.moveToCurrentRow ();
            rs.refreshRow ();
            int id = rs.getInt ("ID");
            rs.close ();
            assertCondition (id == 33);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
refreshRow() - Should clear any warnings.
**/
   public void Var017 ()
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
            rs.refreshRow ();
            SQLWarning after = rs.getWarnings ();
            rs.close ();
            s.close ();
            assertCondition ((before != null) && (after == null));
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



// TEST NOTE: It would be nice to verify that refreshRow() implicity
//            closes a previously retrieved InputStream.  However
//            it is not obvious how to check that an InputStream
//            has been closed.



/**
refreshRow() - Verify that changes in another statement get reflected
when we refresh.
**/
   public void Var018 ()
   {
      if (checkJdbc20 ()) {
         try {
            statement3_.executeUpdate ("UPDATE " + JDRSTest.RSTEST_POS
                                       + " SET VALUE='We love JDBC' WHERE ID=49");
            ResultSet rs = statement3_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_POS);
            rs.absolute (49);
            String s1 = rs.getString ("VALUE");

            Statement statement = connection_.createStatement ();
            statement.executeUpdate ("UPDATE " + JDRSTest.RSTEST_POS
                                     + " SET VALUE='ODBC Fan Club' WHERE ID=49");
            statement.close ();

            rs.refreshRow ();
            String s2 = rs.getString ("VALUE");
            rs.close ();
            assertCondition ((s1.equals ("We love JDBC")) && (s2.equals ("ODBC Fan Club")));
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
refreshRow() - Should have no effect when no updates are pending.
**/
   public void Var019()
   {
      if (checkJdbc20 ()) {
         try {
            statement3_.executeUpdate ("UPDATE " + JDRSTest.RSTEST_POS
                                       + " SET VALUE='1-based indices' WHERE ID=67");
            ResultSet rs = statement3_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_POS + " FOR UPDATE");
            rs.absolute (67);
            rs.refreshRow ();
            String s = rs.getString ("VALUE");
            rs.close ();
            assertCondition (s.equals ("1-based indices"));
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
refreshRow() - Should clear any pending updates.
**/
   public void Var020()
   {
      if (checkJdbc20 ()) {
         try {
            statement3_.executeUpdate ("UPDATE " + JDRSTest.RSTEST_POS
                                       + " SET VALUE='lightning fast.' WHERE ID=61");
            ResultSet rs = statement3_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_POS + " FOR UPDATE");
            rs.absolute (61);
            rs.updateString ("VALUE", "quite slow.");
            rs.refreshRow ();
            rs.updateRow ();
            String s = rs.getString ("VALUE");
            rs.close ();
            assertCondition (s.equals ("lightning fast."));
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
refreshRow() - Should  not preclude the ability to update
afterwards.
**/
   public void Var021()
   {
      if (checkJdbc20 ()) {
         try {
            statement3_.executeUpdate ("UPDATE " + JDRSTest.RSTEST_POS
                                       + " SET VALUE='Y2K compliant' WHERE ID=59");
            ResultSet rs = statement3_.executeQuery ("SELECT * FROM "
                                                     + JDRSTest.RSTEST_POS + " FOR UPDATE");
            rs.absolute (61);
            rs.updateString ("VALUE", "the year 2002.");
            rs.refreshRow ();
            rs.updateString ("VALUE", "again in 2005.");
            rs.updateRow ();
            String s = rs.getString ("VALUE");
            rs.close ();
            assertCondition (s.equals ("again in 2005."));
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



}



