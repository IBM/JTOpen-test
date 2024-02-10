///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSFetchSize.java
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DatabaseMetaData;
import java.util.Hashtable;



/**
Testcase JDRSFetchSize.  This tests the following
methods of the JDBC ResultSet class:

<ul>
<li>getFetchSize()
<li>setFetchSize()
<li>"block criteria" property
<li>"block size" property
<li>"prefetch" property
</ul>
**/
public class JDRSFetchSize
extends JDTestcase {



   // Private data.
   private Statement           statement_;



/**
Constructor.
**/
   public JDRSFetchSize (AS400 systemObject,
                         Hashtable namesAndVars,
                         int runMode,
                         FileOutputStream fileOutputStream,
                         
                         String password)
   {
      super (systemObject, "JDRSFetchSize",
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
         // Pass a block criteria of 0 so that setFetchSize() has
         // an effect.
         connection_ = testDriver_.getConnection (baseURL_
                                                  + ";block criteria=0", userId_, encryptedPassword_,"JDRSFetchSize1");
	 if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
	     // Lite does not support scrollable cursors 
	     statement_ = connection_.createStatement (); 
	 } else { 
	     statement_ = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
						       ResultSet.CONCUR_READ_ONLY);
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
         connection_.close ();
      }
   }



/**
Checks that the result set contains the expected contents.
**/
   private boolean check (ResultSet rs)
   throws SQLException
   {
      int rows = 0;
      boolean check = true;
      while (rs.next ()) {
         ++rows;
         check = check && (rs.getInt ("ID") == rows);
      }
      return((rows == 99) && (check == true));
   }


/**
getFetchSize() - Should throw an exception when the result
set is closed.
**/
   public void Var001 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statement_.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            rs.close ();
            int fetchSize = rs.getFetchSize ();
            failed ("Didn't throw SQLException but got "+fetchSize);
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
getFetchSize() - Should return the default fetch size
when it has not been set (and the statement's fetch size has
not been set either).
**/
   public void Var002 ()
   {
      if (checkJdbc20 ()) {
         try {
            Statement s = connection_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            int fetchSize = rs.getFetchSize ();
            rs.close ();
            s.close ();
            assertCondition (fetchSize == 0);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
getFetchSize() - Should return the fetch size when it
has been set in the statement only.
**/
   public void Var003 ()
   {
      if (checkJdbc20 ()) {
         try {
            Statement s = connection_.createStatement ();
            s.setFetchSize (7);
            ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            int fetchSize = rs.getFetchSize ();
            rs.close ();
            s.close ();
            assertCondition (fetchSize == 7);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
getFetchSize() - Should return the fetch size when it
has been set in the result set only.
**/
   public void Var004 ()
   {
      if (checkJdbc20 ()) {
         try {
            Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                       ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            rs.setFetchSize (9);
            int fetchSize = rs.getFetchSize ();
            rs.close ();
            s.close ();
            assertCondition (fetchSize == 9);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
getFetchSize() - Should return the fetch size when it
has been set in the statement and result set.
**/
   public void Var005 ()
   {
      if (checkJdbc20 ()) {
         try {
            Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                       ResultSet.CONCUR_READ_ONLY);
            s.setFetchSize (5);
            ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            rs.setFetchSize (8);
            int fetchSize = rs.getFetchSize ();
            rs.close ();
            s.close ();
            assertCondition (fetchSize == 8);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }


/**
setFetchSize() - Should throw an exception when the result
set is closed.
**/
   public void Var006 ()
   {
      if (checkJdbc20 ()) {
         try {
            ResultSet rs = statement_.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            rs.close ();
            rs.setFetchSize (4);
            failed ("Didn't throw SQLException set setting fetch size on closed cursor ");
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
setFetchSize() - Pass a fetch size less than 0.  Should throw
an exception.
**/
   public void Var007 ()
   {
      if (checkJdbc20 ()) {
         try {
            Statement s = connection_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            rs.setFetchSize (-1);            
            failed ("Didn't throw SQLException");
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }



/**
setFetchSize() - Pass a fetch size greater than max rows (when max rows
is set).  Should throw an exception.
**/
   public void Var008 ()
   {
      if (checkJdbc20 ()) {
         try {
            Statement s = connection_.createStatement ();
            s.setMaxRows (12);
            ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            rs.setFetchSize (13);
            failed ("Didn't throw SQLException");
         } catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
         }
      }
   }




/**
setFetchSize() - Pass a fetch size of 0.
**/
   public void Var009 ()
   {
      if (checkJdbc20 ()) {
         try {
            Statement s = connection_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT * FROM " 
                                           + JDRSTest.RSTEST_POS + " FOR FETCH ONLY");
            rs.setFetchSize (0);            
            boolean check = check (rs);
            rs.close ();
            s.close ();
            assertCondition (check);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
setFetchSize() - Pass a fetch size of 1.
**/
   public void Var010 ()
   {
      if (checkJdbc20 ()) {
         try {
            Statement s = connection_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT * FROM " 
                                           + JDRSTest.RSTEST_POS + " FOR FETCH ONLY");
            rs.setFetchSize (1);            
            boolean check = check (rs);
            rs.close ();
            s.close ();
            assertCondition (check);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
setFetchSize() - Pass a fetch size less than the number of rows
in the result set.
**/
   public void Var011 ()
   {
      if (checkJdbc20 ()) {
         try {
            Statement s = connection_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT * FROM " 
                                           + JDRSTest.RSTEST_POS + " FOR FETCH ONLY");
            rs.setFetchSize (50);            
            boolean check = check (rs);
            rs.close ();
            s.close ();
            assertCondition (check);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
setFetchSize() - Pass a fetch size equal to the number of rows
in the result set.
**/
   public void Var012 ()
   {
      if (checkJdbc20 ()) {
         try {
            Statement s = connection_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT * FROM " 
                                           + JDRSTest.RSTEST_POS + " FOR FETCH ONLY");
            rs.setFetchSize (99);            
            boolean check = check (rs);
            rs.close ();
            s.close ();
            assertCondition (check);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
setFetchSize() - Pass a fetch size greater than the number of rows
in the result set.
**/
   public void Var013 ()
   {
      if (checkJdbc20 ()) {
         try {
            Statement s = connection_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT * FROM " 
                                           + JDRSTest.RSTEST_POS + " FOR FETCH ONLY");
            rs.setFetchSize (150);            
            boolean check = check (rs);
            rs.close ();
            s.close ();
            assertCondition (check);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
setFetchSize() - Pass a valid fetch size when record blocking
is turned off and the block size is 0.
**/
   public void Var014 ()
   {
      if (checkJdbc20 ()) {
         try {
            Connection c = testDriver_.getConnection (baseURL_ 
                                                      + ";block criteria=0;block size=0", userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT * FROM " 
                                           + JDRSTest.RSTEST_POS + " FOR FETCH ONLY");
            rs.setFetchSize (50);            
            boolean check = check (rs);
            rs.close ();
            s.close ();
            c.close ();
            assertCondition (check);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
"block criteria" property - Specify "0". 
**/
   public void Var015 ()
   {
      if (checkJdbc20 ()) {
         try {
            Connection c = testDriver_.getConnection (baseURL_ 
                                                      + ";block criteria=0", userId_, encryptedPassword_);
            Statement s = c.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                             ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = s.executeQuery ("SELECT * FROM " + JDRSTest.RSTEST_POS);
            boolean check = check (rs);
            rs.close ();
            s.close ();
            c.close ();
            assertCondition (check);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }




/**
"block criteria" property - Specify "1" when FOR FETCH ONLY is
not specified. 
**/
   public void Var016 ()
   {
      if (checkJdbc20 ()) {
         try {
            Connection c = testDriver_.getConnection (baseURL_ 
                                                      + ";block criteria=1", userId_, encryptedPassword_);
            Statement s = c.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                             ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = s.executeQuery ("SELECT * FROM " + JDRSTest.RSTEST_POS);
            boolean check = check (rs);
            rs.close ();
            s.close ();
            c.close ();
            assertCondition (check);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
"block criteria" property - Specify "1" when FOR FETCH ONLY is
specified. 
**/
   public void Var017 ()
   {
      if (checkJdbc20 ()) {
         try {
            Connection c = testDriver_.getConnection (baseURL_ 
                                                      + ";block criteria=1", userId_, encryptedPassword_);
            Statement s = c.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                             ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = s.executeQuery ("SELECT * FROM " 
                                           + JDRSTest.RSTEST_POS + " FOR FETCH ONLY");
            boolean check = check (rs);
            rs.close ();
            s.close ();
            c.close ();
            assertCondition (check);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
"block criteria" property - Specify "2" when FOR UPDATE is
not specified. 
**/
   public void Var018 ()
   {
      if (checkJdbc20 ()) {
         try {
            Connection c = testDriver_.getConnection (baseURL_ 
                                                      + ";block criteria=1", userId_, encryptedPassword_);
            Statement s = c.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                             ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = s.executeQuery ("SELECT * FROM " + JDRSTest.RSTEST_POS);
            boolean check = check (rs);
            rs.close ();
            s.close ();
            c.close ();
            assertCondition (check);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
"block criteria" property - Specify "1" when FOR UPDATE is
specified. 
**/
   public void Var019 ()
   {
      if (checkJdbc20 ()) {
         try {
            Connection c = testDriver_.getConnection (baseURL_ 
                                                      + ";block criteria=1", userId_, encryptedPassword_);
            Statement s = c.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                             ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = s.executeQuery ("SELECT * FROM " 
                                           + JDRSTest.RSTEST_POS + " FOR UPDATE");
            boolean check = check (rs);
            rs.close ();
            s.close ();
            c.close ();
            assertCondition (check);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }


/**
"block criteria" property - Specify a number that is not valid.
**/
   public void Var020 ()
   {
      if (checkJdbc20 ()) {
         try {
            Connection c = testDriver_.getConnection (baseURL_ 
                                                      + ";block criteria=9", userId_, encryptedPassword_);
            Statement s = c.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                             ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = s.executeQuery ("SELECT * FROM " + JDRSTest.RSTEST_POS);
            boolean check = check (rs);
            rs.close ();
            s.close ();
            c.close ();
            assertCondition (check);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }




/**
"block criteria" property - Specify a value that is not a number.
**/
   public void Var021 ()
   {
      if (checkJdbc20 ()) {
         try {
            Connection c = testDriver_.getConnection (baseURL_ 
                                                      + ";block criteria=Monkey", userId_, encryptedPassword_);
            Statement s = c.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                             ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = s.executeQuery ("SELECT * FROM " + JDRSTest.RSTEST_POS);
            boolean check = check (rs);
            rs.close ();
            s.close ();
            c.close ();
            assertCondition (check);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }




/**
"block size" property - Specify "0".
**/
   public void Var022 ()
   {
      if (checkJdbc20 ()) {
         try {
            Connection c = testDriver_.getConnection (baseURL_ 
                                                      + ";block criteria=2;block size=0", userId_, encryptedPassword_);
            Statement s = c.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                             ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = s.executeQuery ("SELECT * FROM " + JDRSTest.RSTEST_POS);
            boolean check = check (rs);
            rs.close ();
            s.close ();
            c.close ();
            assertCondition (check);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }




/**
"block size" property - Specify "8".
**/
   public void Var023 ()
   {
      if (checkJdbc20 ()) {
         try {
            Connection c = testDriver_.getConnection (baseURL_ 
                                                      + ";block criteria=2;block size=8", userId_, encryptedPassword_);
            Statement s = c.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                             ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = s.executeQuery ("SELECT * FROM " + JDRSTest.RSTEST_POS);
            boolean check = check (rs);
            rs.close ();
            s.close ();
            c.close ();
            assertCondition (check);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }




/**
"block size" property - Specify "16".
**/
   public void Var024 ()
   {
      if (checkJdbc20 ()) {
         try {
            Connection c = testDriver_.getConnection (baseURL_ 
                                                      + ";block criteria=2;block size=16", userId_, encryptedPassword_);
            Statement s = c.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                             ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = s.executeQuery ("SELECT * FROM " + JDRSTest.RSTEST_POS);
            boolean check = check (rs);
            rs.close ();
            s.close ();
            c.close ();
            assertCondition (check);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }




/**
"block size" property - Specify "32".
**/
   public void Var025 ()
   {
      if (checkJdbc20 ()) {
         try {
            Connection c = testDriver_.getConnection (baseURL_ 
                                                      + ";block criteria=2;block size=32", userId_, encryptedPassword_);
            Statement s = c.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                             ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = s.executeQuery ("SELECT * FROM " + JDRSTest.RSTEST_POS);
            boolean check = check (rs);
            rs.close ();
            s.close ();
            c.close ();
            assertCondition (check);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }




/**
"block size" property - Specify "64".
**/
   public void Var026 ()
   {
      if (checkJdbc20 ()) {
         try {
            Connection c = testDriver_.getConnection (baseURL_ 
                                                      + ";block criteria=2;block size=64", userId_, encryptedPassword_);
            Statement s = c.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                             ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = s.executeQuery ("SELECT * FROM " + JDRSTest.RSTEST_POS);
            boolean check = check (rs);
            rs.close ();
            s.close ();
            c.close ();
            assertCondition (check);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }




/**
"block size" property - Specify "128".
**/
   public void Var027 ()
   {
      if (checkJdbc20 ()) {
         try {
            Connection c = testDriver_.getConnection (baseURL_ 
                                                      + ";block criteria=2;block size=128", userId_, encryptedPassword_);
            Statement s = c.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                             ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = s.executeQuery ("SELECT * FROM " + JDRSTest.RSTEST_POS);
            boolean check = check (rs);
            rs.close ();
            s.close ();
            c.close ();
            assertCondition (check);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }




/**
"block size" property - Specify "256".
**/
   public void Var028 ()
   {
      if (checkJdbc20 ()) {
         try {
            Connection c = testDriver_.getConnection (baseURL_ 
                                                      + ";block criteria=2;block size=256", userId_, encryptedPassword_);
            Statement s = c.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                             ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = s.executeQuery ("SELECT * FROM " + JDRSTest.RSTEST_POS);
            boolean check = check (rs);
            rs.close ();
            s.close ();
            c.close ();
            assertCondition (check);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }




/**
"block size" property - Specify "512".
**/
   public void Var029 ()
   {
      if (checkJdbc20 ()) {
         try {
            Connection c = testDriver_.getConnection (baseURL_ 
                                                      + ";block criteria=2;block size=512", userId_, encryptedPassword_);
            Statement s = c.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                             ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = s.executeQuery ("SELECT * FROM " + JDRSTest.RSTEST_POS);
            boolean check = check (rs);
            rs.close ();
            s.close ();
            c.close ();
            assertCondition (check);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }




/**
"block size" property - Specify a number that is not valid.
**/
   public void Var030 ()
   {
      if (checkJdbc20 ()) {
         try {
            Connection c = testDriver_.getConnection (baseURL_ 
                                                      + ";block criteria=2;block size=1024", userId_, encryptedPassword_);
            Statement s = c.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                             ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = s.executeQuery ("SELECT * FROM " + JDRSTest.RSTEST_POS);
            int rows = 0;
            boolean check = true;
            while (rs.next ()) {
               ++rows;
               check = check && (rs.getInt ("ID") == rows);
            }
            rs.close ();
            s.close ();
            c.close ();
            assertCondition (check);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }




/**
"block size" property - Specify a value that is not a number.
**/
   public void Var031 ()
   {
      if (checkJdbc20 ()) {
         try {
            Connection c = testDriver_.getConnection (baseURL_ 
                                                      + ";block criteria=2;block size=lion", userId_, encryptedPassword_);
            Statement s = c.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                             ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = s.executeQuery ("SELECT * FROM " + JDRSTest.RSTEST_POS);
            boolean check = check (rs);
            rs.close ();
            s.close ();
            c.close ();
            assertCondition (check);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
"prefetch" property - Specify "false".
**/
   public void Var032 ()
   {
      if (checkJdbc20 ()) {
         try {
            Connection c = testDriver_.getConnection (baseURL_ 
                                                      + ";prefetch=false;block criteria=2;block size=8", userId_, encryptedPassword_);
            Statement s = c.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                             ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = s.executeQuery ("SELECT * FROM " + JDRSTest.RSTEST_POS);
            boolean check = check (rs);
            rs.close ();
            s.close ();
            c.close ();
            assertCondition (check);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
"prefetch" property - Specify "true" with record blocking.
**/
   public void Var033 ()
   {
      if (checkJdbc20 ()) {
         try {
            Connection c = testDriver_.getConnection (baseURL_ 
                                                      + ";prefetch=true;block criteria=2;block size=8", userId_, encryptedPassword_);
            Statement s = c.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                             ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = s.executeQuery ("SELECT * FROM " + JDRSTest.RSTEST_POS);
            boolean check = check (rs);
            rs.close ();
            s.close ();
            c.close ();
            assertCondition (check);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }


/**
"prefetch" property - Specify "true" without record blocking.
**/
   public void Var034 ()
   {
      if (checkJdbc20 ()) {
         try {
            Connection c = testDriver_.getConnection (baseURL_ 
                                                      + ";prefetch=true;block criteria=0", userId_, encryptedPassword_);
            Statement s = c.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                             ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = s.executeQuery ("SELECT * FROM " + JDRSTest.RSTEST_POS);
            boolean check = check (rs);
            rs.close ();
            s.close ();
            c.close ();
            assertCondition (check);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
"prefetch" property - With prefetch set to false, call next() when the ResultSet is empty.
**/
   public void Var035 ()
   {
      if (checkJdbc20 ()) {
         try {
            Connection c = testDriver_.getConnection (baseURL_ 
                                                      + ";prefetch=false", userId_, encryptedPassword_);
            Statement s = c.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                             ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT WHERE 1=2");
            boolean check = rs.next();
            rs.close ();
            s.close ();
            c.close ();
            assertCondition (check==false);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
"prefetch" property - With prefetch set to false, call previous() when the ResultSet is empty.
**/
   public void Var036 ()
   {
      if (checkJdbc20 ()) {
         try {
            Connection c = testDriver_.getConnection (baseURL_ 
                                                      + ";prefetch=false", userId_, encryptedPassword_);
            Statement s = c.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                             ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT WHERE 1=2");
            boolean check = rs.previous();
            rs.close ();
            s.close ();
            c.close ();
            assertCondition (check==false);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
"prefetch" property - With prefetch set to false, call absolute() when the ResultSet is empty.
**/
   public void Var037 ()
   {
      if (checkJdbc20 ()) {
         try {
            Connection c = testDriver_.getConnection (baseURL_ 
                                                      + ";prefetch=false", userId_, encryptedPassword_);
            Statement s = c.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                             ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT WHERE 1=2");
            boolean check = rs.absolute(2);
            rs.close ();
            s.close ();
            c.close ();
            assertCondition (check==false);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
"prefetch" property - With prefetch set to false, call relative() when the ResultSet is empty.
**/
   public void Var038 ()
   {
      if (checkJdbc20 ()) {
         try {
            Connection c = testDriver_.getConnection (baseURL_ 
                                                      + ";prefetch=false", userId_, encryptedPassword_);
            Statement s = c.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                             ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT WHERE 1=2");
            boolean check = rs.relative(3);
            rs.close ();
            s.close ();
            c.close ();
            assertCondition (check==false);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
"prefetch" property - With prefetch set to false, call last() when the ResultSet is empty.
**/
   public void Var039 ()
   {
      if (checkJdbc20 ()) {
         try {
            Connection c = testDriver_.getConnection (baseURL_ 
                                                      + ";prefetch=false", userId_, encryptedPassword_);
            Statement s = c.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                             ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT WHERE 1=2");
            boolean check = rs.last();
            rs.close ();
            s.close ();
            c.close ();
            assertCondition (check==false);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
"prefetch" property - With prefetch set to false, call first() when the ResultSet is empty.
**/
   public void Var040 ()
   {
      if (checkJdbc20 ()) {
         try {
            Connection c = testDriver_.getConnection (baseURL_ 
                                                      + ";prefetch=false", userId_, encryptedPassword_);
            Statement s = c.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                             ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT WHERE 1=2");
            boolean check = rs.first();
            rs.close ();
            s.close ();
            c.close ();
            assertCondition (check==false);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
"prefetch" property - With prefetch set to false, call isFirst() when the ResultSet is empty.
**/
   public void Var041 ()
   {
      if (checkJdbc20 ()) {
         try {
            Connection c = testDriver_.getConnection (baseURL_ 
                                                      + ";prefetch=false", userId_, encryptedPassword_);
            Statement s = c.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                             ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT WHERE 1=2");
            boolean check = rs.isFirst();
            rs.close ();
            s.close ();
            c.close ();
            assertCondition (check==false);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
"prefetch" property - With prefetch set to false, call isLast() when the ResultSet is empty.
**/
   public void Var042 ()
   {
      if (checkJdbc20 ()) {
         try {
            Connection c = testDriver_.getConnection (baseURL_ 
                                                      + ";prefetch=false", userId_, encryptedPassword_);
            Statement s = c.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                             ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT WHERE 1=2");
            boolean check = rs.isLast();
            rs.close ();
            s.close ();
            c.close ();
            assertCondition (check==false);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }




/**
"prefetch" property - With prefetch set to false, call isAfterLast() when the ResultSet is empty.
**/
   public void Var043 ()
   {
      if (checkJdbc20 ()) {
         try {
            Connection c = testDriver_.getConnection (baseURL_ 
                                                      + ";prefetch=false", userId_, encryptedPassword_);
            Statement s = c.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                             ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT WHERE 1=2");
            boolean check = rs.isAfterLast();
            rs.close ();
            s.close ();
            c.close ();
            assertCondition (check==false);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
"prefetch" property - With prefetch set to false, call isBeforeFirst() when the ResultSet is empty.
**/
   public void Var044 ()
   {
      if (checkJdbc20 ()) {
         try {
            Connection c = testDriver_.getConnection (baseURL_ 
                                                      + ";prefetch=false", userId_, encryptedPassword_);
            Statement s = c.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                             ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT WHERE 1=2");
            boolean check = rs.isBeforeFirst();
            rs.close ();
            s.close ();
            c.close ();
            assertCondition (check==false);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
"prefetch" property - With prefetch set to false, call next() when the ResultSet is not empty.
**/
   public void Var045 ()
   {
      if (checkJdbc20 ()) {
         try {
            Connection c = testDriver_.getConnection (baseURL_ 
                                                      + ";prefetch=false", userId_, encryptedPassword_);
            Statement s = c.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                             ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            boolean check = rs.next();
            rs.close ();
            s.close ();
            c.close ();
            assertCondition (check==true);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
"prefetch" property - With prefetch set to false, call previous() when the ResultSet is not empty.
**/
   public void Var046 ()
   {
      if (checkJdbc20 ()) {
         try {
            Connection c = testDriver_.getConnection (baseURL_ 
                                                      + ";prefetch=false", userId_, encryptedPassword_);
            Statement s = c.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                             ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            boolean check = rs.previous();
            rs.close ();
            s.close ();
            c.close ();
            assertCondition (check==false);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
"prefetch" property - With prefetch set to false, call absolute() when the ResultSet is not empty.
**/
   public void Var047 ()
   {
      if (checkJdbc20 ()) {
         try {
            Connection c = testDriver_.getConnection (baseURL_ 
                                                      + ";prefetch=false", userId_, encryptedPassword_);
            Statement s = c.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                             ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            boolean check = rs.absolute(2);
            rs.close ();
            s.close ();
            c.close ();
            assertCondition (check==true);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
"prefetch" property - With prefetch set to false, call relative() when the ResultSet is not empty.
**/
   public void Var048 ()
   {
      
	   //made applicable
	  /*if (isToolboxDriver())
      {
          notApplicable("todo for Toolbox. fix like Native.");
          return;
      }*/
	   
      if (checkJdbc20 ()) {
         try {
            Connection c = testDriver_.getConnection (baseURL_ 
                                                      + ";prefetch=false", userId_, encryptedPassword_);
            Statement s = c.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                             ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");

            boolean check = rs.relative(3);
            rs.close ();
            s.close ();
            c.close ();
            // Note:  Right or wrong, we do not expect relative to not work today when the 
            //        cursor has not already been positioned on a ResultSet row (see 
            //        JDRSRelative var005 for details).
            // Native fixed this bug in V5R5.  Relative should work if there are 3 rows,
            // which there are.
            // Native fixed this bug in V5R4 using SI34211
            if (getRelease() >= JDTestDriver.RELEASE_V5R4M0 || isToolboxDriver()) {
              assertCondition (check==true, "Relative should work when there are 3 more rows.  Fixed by native Driver 01/10/2007");
            } else { 
               assertCondition (check==false);
            }
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
"prefetch" property - With prefetch set to false, call last() when the ResultSet is not empty.
**/
   public void Var049 ()
   {
      if (checkJdbc20 ()) {
         try {
            Connection c = testDriver_.getConnection (baseURL_ 
                                                      + ";prefetch=false", userId_, encryptedPassword_);
            Statement s = c.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                             ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            boolean check = rs.last();
            rs.close ();
            s.close ();
            c.close ();
            assertCondition (check==true);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
"prefetch" property - With prefetch set to false, call first() when the ResultSet is not empty.
**/
   public void Var050 ()
   {
      if (checkJdbc20 ()) {
         try {
            Connection c = testDriver_.getConnection (baseURL_ 
                                                      + ";prefetch=false", userId_, encryptedPassword_);
            Statement s = c.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                             ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            boolean check = rs.first();
            rs.close ();
            s.close ();
            c.close ();
            assertCondition (check==true);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
"prefetch" property - With prefetch set to false, call isFirst() when the ResultSet is not empty.
**/
   public void Var051 ()
   {
      if (checkJdbc20 ()) {
         try {
            Connection c = testDriver_.getConnection (baseURL_ 
                                                      + ";prefetch=false", userId_, encryptedPassword_);
            Statement s = c.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                             ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            boolean check = rs.isFirst();
            rs.close ();
            s.close ();
            c.close ();
            assertCondition (check==false);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
"prefetch" property - With prefetch set to false, call isLast() when the ResultSet is not empty.
**/
   public void Var052 ()
   {
      if (checkJdbc20 ()) {
         try {
            Connection c = testDriver_.getConnection (baseURL_ 
                                                      + ";prefetch=false", userId_, encryptedPassword_);
            Statement s = c.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                             ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            boolean check = rs.isLast();
            rs.close ();
            s.close ();
            c.close ();
            assertCondition (check==false);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }




/**
"prefetch" property - With prefetch set to false, call isAfterLast() when the ResultSet is not empty.
**/
   public void Var053 ()
   {
      if (checkJdbc20 ()) {
         try {
            Connection c = testDriver_.getConnection (baseURL_ 
                                                      + ";prefetch=false", userId_, encryptedPassword_);
            Statement s = c.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                             ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            boolean check = rs.isAfterLast();
            rs.close ();
            s.close ();
            c.close ();
            assertCondition (check==false);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
"prefetch" property - With prefetch set to false, call isBeforeFirst() when the ResultSet is not empty.
**/
   public void Var054 ()
   {
      if (checkJdbc20 ()) {
         try {
            Connection c = testDriver_.getConnection (baseURL_ 
                                                      + ";prefetch=false", userId_, encryptedPassword_);
            Statement s = c.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                             ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = s.executeQuery ("SELECT * FROM QIWS.QCUSTCDT");
            boolean check = rs.isBeforeFirst();
            rs.close ();
            s.close ();
            c.close ();
            assertCondition (check==true);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
"prefetch" property - With prefetch set to false, verify that we can get all the rows in a ResultSet
**/
   public void Var055 ()
   {
      if (checkJdbc20 ()) {
         try {
            Connection c = testDriver_.getConnection (baseURL_ 
                                                      + ";prefetch=false", userId_, encryptedPassword_);
            Statement s = c.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT * FROM " + JDRSTest.RSTEST_POS);
            boolean check = check (rs);
            rs.close ();
            s.close ();
            c.close ();
            assertCondition (check==true);
         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }



/**
"prefetch" property - With prefetch set to false, verify that we can get data from metadata ResultSets.
**/
   public void Var056 ()
   {
      if (checkJdbc20 ()) {
         try {
            Connection c = testDriver_.getConnection (baseURL_
						      + ";prefetch=false", userId_, encryptedPassword_);
	    DatabaseMetaData dmd = c.getMetaData();
            String types[]  = {"TABLE", "VIEW", "SYSTEM TABLE"};
            ResultSet rs = dmd.getTables(null, "QSYS2", "SYS%", types);
	    	    
            int count = 0;
	    while (rs.next()) {
	       rs.getString(3);
	       count++;
            }
            rs.close ();
            c.close ();
            assertCondition (count > 0);

         } catch (Exception e) {
            failed (e, "Unexpected Exception");
         }
      }
   }


/**
"block size" property - Specify "512" and wide table.
Make sure we can fetch without an error 
**/
   public void Var057 ()
   {
      if (checkJdbc20 ()) {
         try {
            Connection c = testDriver_.getConnection (baseURL_ 
                                                      + ";block criteria=2;block size=512", userId_, encryptedPassword_);
            Statement s = c.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                             ResultSet.CONCUR_READ_ONLY);
	    String collection = collection_;
	    if (collection == null) {
		collection = "QGPL"; 
	    }
	    // String procedure = collection+".JDRSMISC43";

	    String table = collection+".JDRSFETCHSIZE057"; 
	    initTable(s, table," (c1 char(16000), c2 char(16000), c3 char(765))");
	    

	    s.executeUpdate("INSERT INTO  "+table+" VALUES('1', '2', '3')");

            ResultSet rs = s.executeQuery ("SELECT * FROM " + table);
	    rs.next();
	    String c1 = rs.getString(1);
	    if (c1 != null) c1 = c1.trim(); 
	    String c2 = rs.getString(2);
	    if (c2 != null) c2 = c2.trim(); 
	    String c3 = rs.getString(3);
	    if (c3 != null) c3 = c3.trim(); 
            rs.close ();

	    cleanupTable(s,table); 
            s.close ();
            c.close ();
	    boolean check = "1".equals(c1) && "2".equals(c2) && "3".equals(c3); 
            assertCondition (check, "OUTPUT ("+c1+","+c2+","+c3+") != (1,2,3)");
         } catch (Exception e) {
            failed (e, "Unexpected Exception -- New testcase added by native driver 4/26/2004 -- Exposes pre V5R3 bug in allocateResultSetMemoryForBlocking ");
         }
      }
   }

}
